import apiClient from './axios'

export const getApplications = () => {
  return apiClient.get('/applications')
}

export const getApplicationById = (id) => {
  return apiClient.get(`/applications/${id}`)
}

export const getApplicationsByCandidate = (candidateId) => {
  return apiClient.get(`/applications/candidate/${candidateId}`)
}

export const createApplication = (data) => {
  return apiClient.post('/applications', data)
}

export const updateApplication = (id, data) => {
  return apiClient.put(`/applications/${id}`, data)
}

export const deleteApplication = (id) => {
  return apiClient.delete(`/applications/${id}`)
}

export const searchApplications = (keyword) => {
  return apiClient.get(`/applications/search?keyword=${keyword}`)
}
