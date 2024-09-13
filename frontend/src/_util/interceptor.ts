// lib/httpInterceptor.ts
export async function fetchWithAuth(url: string, options: RequestInit = {}) {
  const user = typeof window !== 'undefined' ? JSON.parse(localStorage.getItem('userdetails') || '{}') : null;
  const token = typeof window !== 'undefined' ? localStorage.getItem('Authorization') : null;
  const xsrfToken = typeof window !== 'undefined' ? localStorage.getItem('XSRF-TOKEN') : null;

  const headers = new Headers({
    'Content-Type': 'application/json',
    ...user && user.email && user.password ? { 'Authorization': `Basic ${window.btoa(user.email + ':' + user.password)}` } : {},
    ...token ? { 'Authorization': token } : {},
    ...xsrfToken ? { 'X-XSRF-TOKEN': xsrfToken } : {},
    'X-Requested-With': 'XMLHttpRequest',
  });

  const response = await fetch(url, {
    ...options,
    headers: {
      ...Object.fromEntries(headers),
      ...options.headers,
    }
  });

  if (!response.ok) {
    if (response.status === 401) {
      if (typeof window !== 'undefined') {
        console.log("test2")
        window.location.href = '/login';
      }
    }
    throw new Error('Network response was not ok');
  }

  return response.json();
}
