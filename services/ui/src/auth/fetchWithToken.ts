export function fetchWithToken(
  url: RequestInfo,
  init?: RequestInit,
  token?: string
) {
  return fetch(url, {
    ...init,
    headers: {
      ...init?.headers,
      Authorization: `Bearer ${token}`,
    },
  });
}
