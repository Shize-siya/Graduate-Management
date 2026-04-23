<template>
  <div class="applications-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>报考情况管理</span>
          <el-button type="primary" @click="handleAdd">新增报考</el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索报考学校、专业"
          style="width: 300px;"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button icon="Search" @click="handleSearch" />
          </template>
        </el-input>
      </div>

      <el-table :data="applications" stripe border style="margin-top: 20px;">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="candidateId" label="考生ID" width="80" />
        <el-table-column prop="examYear" label="报考年份" width="100" />
        <el-table-column prop="firstChoiceSchool" label="第一志愿学校" width="150" />
        <el-table-column prop="firstChoiceMajor" label="第一志愿专业" width="150" />
        <el-table-column prop="secondChoiceSchool" label="第二志愿学校" width="150" />
        <el-table-column prop="secondChoiceMajor" label="第二志愿专业" width="150" />
        <el-table-column prop="initialExamScore" label="初试成绩" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      @close="handleDialogClose"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="考生ID" prop="candidateId">
          <el-input v-model="form.candidateId" type="number" />
        </el-form-item>
        <el-form-item label="报考年份" prop="examYear">
          <el-input v-model="form.examYear" placeholder="如: 2024" />
        </el-form-item>
        <el-form-item label="第一志愿学校" prop="firstChoiceSchool">
          <el-input v-model="form.firstChoiceSchool" />
        </el-form-item>
        <el-form-item label="第一志愿专业" prop="firstChoiceMajor">
          <el-input v-model="form.firstChoiceMajor" />
        </el-form-item>
        <el-form-item label="第二志愿学校" prop="secondChoiceSchool">
          <el-input v-model="form.secondChoiceSchool" />
        </el-form-item>
        <el-form-item label="第二志愿专业" prop="secondChoiceMajor">
          <el-input v-model="form.secondChoiceMajor" />
        </el-form-item>
        <el-form-item label="考试科目1" prop="examSubject1">
          <el-input v-model="form.examSubject1" />
        </el-form-item>
        <el-form-item label="考试科目2" prop="examSubject2">
          <el-input v-model="form.examSubject2" />
        </el-form-item>
        <el-form-item label="考试科目3" prop="examSubject3">
          <el-input v-model="form.examSubject3" />
        </el-form-item>
        <el-form-item label="考试科目4" prop="examSubject4">
          <el-input v-model="form.examSubject4" />
        </el-form-item>
        <el-form-item label="初试成绩" prop="initialExamScore">
          <el-input v-model="form.initialExamScore" type="number" />
        </el-form-item>
        <el-form-item label="复核成绩" prop="recheckExamScore">
          <el-input v-model="form.recheckExamScore" type="number" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="初审中" value="初审中" />
            <el-option label="初试通过" value="初试通过" />
            <el-option label="初试未通过" value="初试未通过" />
            <el-option label="待录取" value="待录取" />
            <el-option label="已录取" value="已录取" />
            <el-option label="未录取" value="未录取" />
          </el-select>
        </el-form-item>
        <el-form-item label="考试地点" prop="examLocation">
          <el-input v-model="form.examLocation" />
        </el-form-item>
        <el-form-item label="考试日期" prop="examDate">
          <el-input v-model="form.examDate" placeholder="如: 2024-12-21" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getApplications,
  createApplication,
  updateApplication,
  deleteApplication,
  searchApplications
} from '../api/application'

const applications = ref([])
const searchKeyword = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref(null)
const currentId = ref(null)

const form = reactive({
  candidateId: null,
  examYear: '',
  firstChoiceSchool: '',
  firstChoiceMajor: '',
  secondChoiceSchool: '',
  secondChoiceMajor: '',
  examSubject1: '',
  examSubject2: '',
  examSubject3: '',
  examSubject4: '',
  initialExamScore: null,
  recheckExamScore: null,
  status: '初审中',
  examLocation: '',
  examDate: '',
  remarks: ''
})

const rules = {
  candidateId: [{ required: true, message: '请输入考生ID', trigger: 'blur' }],
  examYear: [{ required: true, message: '请输入报考年份', trigger: 'blur' }],
  firstChoiceSchool: [{ required: true, message: '请输入第一志愿学校', trigger: 'blur' }],
  firstChoiceMajor: [{ required: true, message: '请输入第一志愿专业', trigger: 'blur' }]
}

const getStatusType = (status) => {
  const types = {
    '初审中': 'info',
    '初试通过': 'success',
    '初试未通过': 'danger',
    '待录取': 'warning',
    '已录取': 'success',
    '未录取': 'danger'
  }
  return types[status] || 'info'
}

const fetchApplications = async () => {
  try {
    const response = await getApplications()
    if (response.data.success) {
      applications.value = response.data.data
    }
  } catch (error) {
    ElMessage.error('获取报考列表失败')
  }
}

const handleSearch = async () => {
  if (!searchKeyword.value.trim()) {
    fetchApplications()
    return
  }
  try {
    const response = await searchApplications(searchKeyword.value)
    if (response.data.success) {
      applications.value = response.data.data
    }
  } catch (error) {
    ElMessage.error('搜索失败')
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增报考'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑报考'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该报考信息吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await deleteApplication(row.id)
    if (response.data.success) {
      ElMessage.success('删除成功')
      fetchApplications()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = { ...form }
        let response
        if (isEdit.value) {
          response = await updateApplication(currentId.value, data)
        } else {
          response = await createApplication(data)
        }

        if (response.data.success) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          fetchApplications()
        } else {
          ElMessage.error(response.data.message)
        }
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

const handleDialogClose = () => {
  resetForm()
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    candidateId: null,
    examYear: '',
    firstChoiceSchool: '',
    firstChoiceMajor: '',
    secondChoiceSchool: '',
    secondChoiceMajor: '',
    examSubject1: '',
    examSubject2: '',
    examSubject3: '',
    examSubject4: '',
    initialExamScore: null,
    recheckExamScore: null,
    status: '初审中',
    examLocation: '',
    examDate: '',
    remarks: ''
  })
}

onMounted(() => {
  fetchApplications()
})
</script>

<style scoped>
.applications-page {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 10px;
}
</style>
