import apiClient from './axios'

export const getAdmissions = () => {
  return apiClient.get('/admissions')
}

export const getAdmissionById = (id) => {
  return apiClient.get(`/admissions/${id}`)
}

export const getAdmissionsByCandidate = (candidateId) => {
  return apiClient.get(`/admissions/candidate/${candidateId}`)
}

export const createAdmission = (data) => {
  return apiClient.post('/admissions', data)
}

export const updateAdmission = (id, data) => {
  return apiClient.put(`/admissions/${id}`, data)
}

export const deleteAdmission = (id) => {
  return apiClient.delete(`/admissions/${id}`)
}

export const searchAdmissions = (keyword) => {
  return apiClient.get(`/admissions/search?keyword=${keyword}`)
}
