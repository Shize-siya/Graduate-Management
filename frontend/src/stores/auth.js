import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, register } from '../api/auth'
import router from '../router'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

  const handleLogin = async (loginData) => {
    try {
      const response = await login(loginData)
      if (response.data.success) {
        token.value = response.data.data.token
        userInfo.value = response.data.data
        localStorage.setItem('token', response.data.data.token)
        localStorage.setItem('userInfo', JSON.stringify(response.data.data))
        router.push('/dashboard')
        return { success: true, message: '登录成功' }
      } else {
        return { success: false, message: response.data.message }
      }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '登录失败' }
    }
  }

  const handleLogout = () => {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.push('/login')
  }

  const handleRegister = async (registerData) => {
    try {
      const response = await register(registerData)
      if (response.data.success) {
        return { success: true, message: '注册成功' }
      } else {
        return { success: false, message: response.data.message }
      }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '注册失败' }
    }
  }

  const isAuthenticated = () => {
    return !!token.value
  }

  return {
    token,
    userInfo,
    handleLogin,
    handleRegister,
    handleLogout,
    isAuthenticated
  }
})
