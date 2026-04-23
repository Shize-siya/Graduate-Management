import apiClient from './axios'

export const login = (data) => {
  return apiClient.post('/auth/login', data)
}

export const register = (data) => {
  return apiClient.post('/auth/register', data)
}
