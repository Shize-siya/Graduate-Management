import apiClient from './axios'

export const getStatistics = () => {
  return apiClient.get('/statistics')
}
