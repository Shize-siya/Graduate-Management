<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon candidates">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalCandidates }}</div>
            <div class="stat-label">考生总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon applications">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalApplications }}</div>
            <div class="stat-label">报考总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon admissions">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalAdmissions }}</div>
            <div class="stat-label">录取总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon pending">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.pendingApplications }}</div>
            <div class="stat-label">待审核</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>年度报考统计</span>
          </template>
          <div ref="applicationChartRef" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>年度录取统计</span>
          </template>
          <div ref="admissionChartRef" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>快速操作</span>
          </template>
          <div class="quick-actions">
            <el-button type="primary" @click="$router.push('/candidates')">考生信息录入</el-button>
            <el-button type="success" @click="$router.push('/applications')">报考信息管理</el-button>
            <el-button type="warning" @click="$router.push('/admissions')">录取信息管理</el-button>
            <el-button type="info" @click="$router.push('/statistics')">查看数据分析</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { User, Document, CircleCheck, Clock } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getStatistics } from '../api/statistics'

const stats = reactive({
  totalCandidates: 0,
  totalApplications: 0,
  totalAdmissions: 0,
  pendingApplications: 0,
  application2024: 0,
  application2025: 0,
  admission2024: 0,
  admission2025: 0
})

const applicationChartRef = ref(null)
const admissionChartRef = ref(null)

const fetchStatistics = async () => {
  try {
    const response = await getStatistics()
    if (response.data.success) {
      Object.assign(stats, response.data.data)
      initCharts()
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

const initCharts = () => {
  if (applicationChartRef.value) {
    const applicationChart = echarts.init(applicationChartRef.value)
    applicationChart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: {
        type: 'category',
        data: ['2024年', '2025年']
      },
      yAxis: { type: 'value' },
      series: [
        {
          name: '报考人数',
          type: 'bar',
          data: [stats.application2024, stats.application2025],
          itemStyle: { color: '#409eff' }
        }
      ]
    })
  }

  if (admissionChartRef.value) {
    const admissionChart = echarts.init(admissionChartRef.value)
    admissionChart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: {
        type: 'category',
        data: ['2024年', '2025年']
      },
      yAxis: { type: 'value' },
      series: [
        {
          name: '录取人数',
          type: 'bar',
          data: [stats.admission2024, stats.admission2025],
          itemStyle: { color: '#67c23a' }
        }
      ]
    })
  }
}

onMounted(() => {
  fetchStatistics()
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  color: white;
  margin-right: 20px;
}

.stat-icon.candidates { background: linear-gradient(135deg, #667eea, #764ba2); }
.stat-icon.applications { background: linear-gradient(135deg, #409eff, #66b1ff); }
.stat-icon.admissions { background: linear-gradient(135deg, #67c23a, #85ce61); }
.stat-icon.pending { background: linear-gradient(135deg, #e6a23c, #ebb563); }

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 5px;
}

.quick-actions {
  display: flex;
  gap: 15px;
}
</style>
