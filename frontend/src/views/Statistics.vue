<template>
  <div class="statistics-page">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.totalCandidates }}</div>
          <div class="stat-label">考生总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.totalApplications }}</div>
          <div class="stat-label">报考总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.totalAdmissions }}</div>
          <div class="stat-label">录取总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.pendingApplications }}</div>
          <div class="stat-label">待审核</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>报考与录取对比</span>
          </template>
          <div ref="trendChartRef" style="width: 100%; height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>录取比例分析</span>
          </template>
          <div ref="pieChartRef" style="width: 100%; height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>考生状态分布</span>
          </template>
          <div ref="yearChartRef" style="width: 100%; height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>关键指标</span>
          </template>
          <div class="metrics">
            <div class="metric-item">
              <div class="metric-label">考生总数</div>
              <div class="metric-value">{{ stats.totalCandidates }}</div>
            </div>
            <div class="metric-item">
              <div class="metric-label">报考总数</div>
              <div class="metric-value">{{ stats.totalApplications }}</div>
            </div>
            <div class="metric-item">
              <div class="metric-label">录取总数</div>
              <div class="metric-value">{{ stats.totalAdmissions }}</div>
            </div>
            <div class="metric-item">
              <div class="metric-label">待审核</div>
              <div class="metric-value">{{ stats.pendingApplications }}</div>
            </div>
            <div class="metric-item">
              <div class="metric-label">已录取</div>
              <div class="metric-value">{{ stats.totalAdmissions }}</div>
            </div>
            <div class="metric-item">
              <div class="metric-label">未录取</div>
              <div class="metric-value">{{ stats.totalApplications - stats.totalAdmissions }}</div>
            </div>
            <div class="metric-item" v-if="stats.totalApplications > 0">
              <div class="metric-label">总体录取率</div>
              <div class="metric-value">{{ ((stats.totalAdmissions / stats.totalApplications) * 100).toFixed(1) }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
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
  admission2025: 0,
  pendingReview2025: 0,
  admitted2024: 0,
  notAdmitted2024: 0
})

const trendChartRef = ref(null)
const pieChartRef = ref(null)
const yearChartRef = ref(null)

const fetchStatistics = async () => {
  try {
    const response = await getStatistics()
    if (response.data.success) {
      Object.assign(stats, response.data.data)
      nextTick(() => {
        initCharts()
      })
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

const initCharts = () => {
  initTrendChart()
  initPieChart()
  initYearChart()
}

const initTrendChart = () => {
  if (!trendChartRef.value) return

  const trendChart = echarts.init(trendChartRef.value)
  trendChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    legend: {
      data: ['报考人数', '录取人数']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['考生', '报考', '录取']
    },
    yAxis: {
      type: 'value',
      name: '人数'
    },
    series: [
      {
        name: '报考人数',
        type: 'bar',
        data: [stats.totalCandidates, stats.totalApplications, stats.totalAdmissions],
        itemStyle: { color: '#409eff' },
        barWidth: 50
      }
    ]
  })
}

const initPieChart = () => {
  if (!pieChartRef.value) return

  const pieChart = echarts.init(pieChartRef.value)
  const admitted = stats.totalAdmissions
  const notAdmitted = stats.totalApplications - stats.totalAdmissions
  const pending = stats.pendingApplications

  pieChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '录取情况',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}: {c}'
        },
        data: [
          { value: admitted, name: '已录取', itemStyle: { color: '#67c23a' } },
          { value: notAdmitted, name: '未录取', itemStyle: { color: '#f56c6c' } },
          { value: pending, name: '待审核', itemStyle: { color: '#e6a23c' } }
        ]
      }
    ]
  })
}

const initYearChart = () => {
  if (!yearChartRef.value) return

  const yearChart = echarts.init(yearChartRef.value)
  yearChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '状态分布',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}: {c}'
        },
        data: [
          { value: stats.totalCandidates, name: '考生总数', itemStyle: { color: '#409eff' } },
          { value: stats.totalApplications, name: '报考总数', itemStyle: { color: '#67c23a' } },
          { value: stats.totalAdmissions, name: '录取总数', itemStyle: { color: '#e6a23c' } },
          { value: stats.pendingApplications, name: '待审核', itemStyle: { color: '#f56c6c' } }
        ]
      }
    ]
  })
}

onMounted(() => {
  fetchStatistics()

  window.addEventListener('resize', () => {
    if (trendChartRef.value) echarts.getInstanceByDom(trendChartRef.value)?.resize()
    if (pieChartRef.value) echarts.getInstanceByDom(pieChartRef.value)?.resize()
    if (yearChartRef.value) echarts.getInstanceByDom(yearChartRef.value)?.resize()
  })
})
</script>

<style scoped>
.statistics-page {
  padding: 0;
}

.stat-card {
  text-align: center;
  padding: 20px;
}

.stat-card :deep(.el-card__body) {
  padding: 20px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 10px;
}

.metrics {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.metric-item {
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
  text-align: center;
}

.metric-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}
</style>
