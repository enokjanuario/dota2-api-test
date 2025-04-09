import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

const errorRate = new Rate('error_rate');
const heroesRequestDuration = new Trend('heroes_request_duration');

export const options = {
  stages: [
    { duration: '10s', target: 1 },
    { duration: '20s', target: 3 },
    { duration: '20s', target: 5 },
    { duration: '10s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<1000'],
    error_rate: ['rate<0.2'],
    heroes_request_duration: ['p(95)<800'],
  }
};

const BASE_URL = 'https://api.opendota.com/api';

export default function () {
  sleep(1 + Math.random());

  const heroesRes = http.get(`${BASE_URL}/heroes`);

  heroesRequestDuration.add(heroesRes.timings.duration);

  const heroesSuccessful = check(heroesRes, {
    'status is 200': (r) => r.status === 200,
    'body is not empty': (r) => r.body.length > 0,
    'body is valid JSON': (r) => {
      try {
        JSON.parse(r.body);
        return true;
      } catch (e) {
        return false;
      }
    },
    'contains heroes data': (r) => {
      try {
        const data = JSON.parse(r.body);
        return Array.isArray(data) && data.length > 0 && data[0].hasOwnProperty('id');
      } catch (e) {
        return false;
      }
    },
  });

  errorRate.add(!heroesSuccessful);

  sleep(5);

}

export function handleSummary(data) {
  return {
    "heroes_performance_report.html": htmlReport(data),
  };
}