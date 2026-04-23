import apiClient from './axios'

export const getCandidates = () => {
  return apiClient.get('/candidates')
}

export const getCandidateById = (id) => {
  return apiClient.get(`/candidates/${id}`)
}

export const createCandidate = (data) => {
  return apiClient.post('/candidates', data)
}

export const updateCandidate = (id, data) => {
  return apiClient.put(`/candidates/${id}`, data)
}

export const deleteCandidate = (id) => {
  return apiClient.delete(`/candidates/${id}`)
}

export const searchCandidates = (keyword) => {
  return apiClient.get(`/candidates/search?keyword=${keyword}`)
}
