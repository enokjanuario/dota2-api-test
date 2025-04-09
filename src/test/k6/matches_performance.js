import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

const errorRate = new Rate('error_rate');
const matchesRequestDuration = new Trend('matches_request_duration');
const matchDetailsRequestDuration = new Trend('match_details_request_duration');

export const options = {
  stages: [
    { duration: '10s', target: 1 },
    { duration: '15s', target: 2 },
    { duration: '15s', target: 3 },
    { duration: '10s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<1200'],
    error_rate: ['rate<0.25'],
    matches_request_duration: ['p(95)<1000'],
    match_details_request_duration: ['p(95)<1000'],
  }
};

const BASE_URL = 'https://api.opendota.com/api';
let matchIds = [];

export default function () {
  sleep(1 + Math.random() * 2);

  const matchesRes = http.get(`${BASE_URL}/publicMatches`);

  matchesRequestDuration.add(matchesRes.timings.duration);

  const matchesSuccessful = check(matchesRes, {
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
    'contains matches data': (r) => {
      try {
        const data = JSON.parse(r.body);
        return Array.isArray(data) && data.length > 0 && data[0].hasOwnProperty('match_id');
      } catch (e) {
        return false;
      }
    },
  });

  errorRate.add(!matchesSuccessful);

  sleep(2);

  if (matchesSuccessful) {
    try {
      const data = JSON.parse(matchesRes.body);
      if (data.length > 0) {
        matchIds = data.slice(0, 3).map(match => match.match_id);

        if (matchIds.length > 0 && Math.random() > 0.5) {
          const randomIndex = Math.floor(Math.random() * matchIds.length);
          const matchDetailsRes = http.get(`${BASE_URL}/matches/${matchIds[randomIndex]}`);

          matchDetailsRequestDuration.add(matchDetailsRes.timings.duration);

          const matchDetailsSuccessful = check(matchDetailsRes, {
            'match details status is 200': (r) => r.status === 200,
            'match details body is not empty': (r) => r.body.length > 0,
            'match details contains valid data': (r) => {
              try {
                const details = JSON.parse(r.body);
                return details.hasOwnProperty('match_id');
              } catch (e) {
                return false;
              }
            },
          });

          errorRate.add(!matchDetailsSuccessful);
        }
      }
    } catch (e) {
      console.error('Error processing matches data:', e);
    }
  }

  sleep(6);

}

export function handleSummary(data) {
  return {
    "matches_performance_report.html": htmlReport(data),
  };
}