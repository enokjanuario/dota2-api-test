import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate, Trend } from 'k6/metrics';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

const errorRate = new Rate('error_rate');
const teamsRequestDuration = new Trend('teams_request_duration');
const teamDetailsRequestDuration = new Trend('team_details_request_duration');
const playersRequestDuration = new Trend('players_request_duration');

export const options = {
  stages: [
    { duration: '10s', target: 1 },
    { duration: '15s', target: 2 },
    { duration: '10s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<1500'],
    error_rate: ['rate<0.25'],
    teamsRequestDuration: ['p(95)<1200'],
    teamDetailsRequestDuration: ['p(95)<1200'],
    playersRequestDuration: ['p(95)<1200'],
  }
};

const BASE_URL = 'https://api.opendota.com/api';
const KNOWN_TEAM_IDS = [15, 39, 2163, 111474, 350190];
const KNOWN_PLAYER_IDS = [86745912, 105248644, 73562326];

export default function () {
  sleep(2 + Math.random() * 3);

  const testTeamsOrPlayers = Math.random() > 0.5;

  if (testTeamsOrPlayers) {
    group('Teams API tests', function () {
      const teamsRes = http.get(`${BASE_URL}/teams`);

      teamsRequestDuration.add(teamsRes.timings.duration);

      const teamsSuccessful = check(teamsRes, {
        'teams status is 200': (r) => r.status === 200,
        'teams body is not empty': (r) => r.body.length > 0,
        'teams contains valid data': (r) => {
          try {
            const data = JSON.parse(r.body);
            return Array.isArray(data) && data.length > 0 && data[0].hasOwnProperty('team_id');
          } catch (e) {
            return false;
          }
        },
      });

      errorRate.add(!teamsSuccessful);

      sleep(3);

      if (teamsSuccessful && Math.random() < 0.3) {
        const randomTeamId = KNOWN_TEAM_IDS[Math.floor(Math.random() * KNOWN_TEAM_IDS.length)];
        const teamDetailsRes = http.get(`${BASE_URL}/teams/${randomTeamId}`);

        teamDetailsRequestDuration.add(teamDetailsRes.timings.duration);

        const teamDetailsSuccessful = check(teamDetailsRes, {
          'team details status is 200': (r) => r.status === 200,
          'team details contains valid data': (r) => {
            try {
              const team = JSON.parse(r.body);
              return team.hasOwnProperty('team_id');
            } catch (e) {
              return false;
            }
          },
        });

        errorRate.add(!teamDetailsSuccessful);
      }
    });
  } else {
    sleep(2);

    group('Players API tests', function () {
      const randomPlayerId = KNOWN_PLAYER_IDS[Math.floor(Math.random() * KNOWN_PLAYER_IDS.length)];

      const playerRes = http.get(`${BASE_URL}/players/${randomPlayerId}`);

      playersRequestDuration.add(playerRes.timings.duration);

      const playerSuccessful = check(playerRes, {
        'player status is 200': (r) => r.status === 200,
        'player contains valid data': (r) => {
          try {
            const player = JSON.parse(r.body);
            return player.hasOwnProperty('profile') &&
                  player.profile !== null;
          } catch (e) {
            return false;
          }
        },
      });

      errorRate.add(!playerSuccessful);

      if (playerSuccessful && Math.random() < 0.2) {
        sleep(2);

        const wlRes = http.get(`${BASE_URL}/players/${randomPlayerId}/wl`);

        const wlSuccessful = check(wlRes, {
          'win/loss status is 200': (r) => r.status === 200,
          'win/loss contains valid data': (r) => {
            try {
              const wl = JSON.parse(r.body);
              return wl.hasOwnProperty('win') && wl.hasOwnProperty('lose');
            } catch (e) {
              return false;
            }
          },
        });

        errorRate.add(!wlSuccessful);
      }
    });
  }

  sleep(7);

}

export function handleSummary(data) {
  return {
    "teams_players_performance_report.html": htmlReport(data),
  };
}