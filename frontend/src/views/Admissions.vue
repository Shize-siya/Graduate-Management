<template>
  <div class="admissions-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>录取情况管理</span>
          <el-button type="primary" @click="handleAdd">新增录取</el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索录取学校、专业"
          style="width: 300px;"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button icon="Search" @click="handleSearch" />
          </template>
        </el-input>
      </div>

      <el-table :data="admissions" stripe border style="margin-top: 20px;">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="candidateId" label="考生ID" width="80" />
        <el-table-column prop="applicationId" label="报考ID" width="80" />
        <el-table-column prop="admissionYear" label="录取年份" width="100" />
        <el-table-column prop="admittedSchool" label="录取学校" width="150" />
        <el-table-column prop="admittedMajor" label="录取专业" width="150" />
        <el-table-column prop="admissionType" label="录取类型" width="100" />
        <el-table-column prop="supervisor" label="导师" width="100" />
        <el-table-column prop="admissionScore" label="录取分数" width="100" />
        <el-table-column prop="totalScore" label="总分" width="100" />
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
        <el-form-item label="报考ID" prop="applicationId">
          <el-input v-model="form.applicationId" type="number" />
        </el-form-item>
        <el-form-item label="录取年份" prop="admissionYear">
          <el-input v-model="form.admissionYear" placeholder="如: 2024" />
        </el-form-item>
        <el-form-item label="录取学校" prop="admittedSchool">
          <el-input v-model="form.admittedSchool" />
        </el-form-item>
        <el-form-item label="录取专业" prop="admittedMajor">
          <el-input v-model="form.admittedMajor" />
        </el-form-item>
        <el-form-item label="录取类型" prop="admissionType">
          <el-select v-model="form.admissionType" placeholder="请选择录取类型">
            <el-option label="全日制" value="全日制" />
            <el-option label="非全日制" value="非全日制" />
          </el-select>
        </el-form-item>
        <el-form-item label="导师" prop="supervisor">
          <el-input v-model="form.supervisor" />
        </el-form-item>
        <el-form-item label="录取分数" prop="admissionScore">
          <el-input v-model="form.admissionScore" type="number" />
        </el-form-item>
        <el-form-item label="总分" prop="totalScore">
          <el-input v-model="form.totalScore" type="number" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="已录取" value="已录取" />
            <el-option label="待报到" value="待报到" />
            <el-option label="已报到" value="已报到" />
            <el-option label="已入学" value="已入学" />
            <el-option label="已退学" value="已退学" />
          </el-select>
        </el-form-item>
        <el-form-item label="入学日期" prop="enrollmentDate">
          <el-input v-model="form.enrollmentDate" placeholder="如: 2024-09-01" />
        </el-form-item>
        <el-form-item label="学费标准" prop="tuitionStandard">
          <el-input v-model="form.tuitionStandard" type="number" />
        </el-form-item>
        <el-form-item label="奖学金等级" prop="scholarshipLevel">
          <el-select v-model="form.scholarshipLevel" placeholder="请选择奖学金等级">
            <el-option label="一等奖学金" value="一等奖学金" />
            <el-option label="二等奖学金" value="二等奖学金" />
            <el-option label="三等奖学金" value="三等奖学金" />
            <el-option label="无奖学金" value="无奖学金" />
          </el-select>
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
  getAdmissions,
  createAdmission,
  updateAdmission,
  deleteAdmission,
  searchAdmissions
} from '../api/admission'

const admissions = ref([])
const searchKeyword = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref(null)
const currentId = ref(null)

const form = reactive({
  candidateId: null,
  applicationId: null,
  admissionYear: '',
  admittedSchool: '',
  admittedMajor: '',
  admissionType: '全日制',
  supervisor: '',
  admissionScore: null,
  totalScore: null,
  status: '已录取',
  enrollmentDate: '',
  tuitionStandard: null,
  scholarshipLevel: '无奖学金',
  remarks: ''
})

const rules = {
  candidateId: [{ required: true, message: '请输入考生ID', trigger: 'blur' }],
  admissionYear: [{ required: true, message: '请输入录取年份', trigger: 'blur' }],
  admittedSchool: [{ required: true, message: '请输入录取学校', trigger: 'blur' }],
  admittedMajor: [{ required: true, message: '请输入录取专业', trigger: 'blur' }]
}

const getStatusType = (status) => {
  const types = {
    '已录取': 'success',
    '待报到': 'warning',
    '已报到': 'primary',
    '已入学': 'success',
    '已退学': 'danger'
  }
  return types[status] || 'info'
}

const fetchAdmissions = async () => {
  try {
    const response = await getAdmissions()
    if (response.data.success) {
      admissions.value = response.data.data
    }
  } catch (error) {
    ElMessage.error('获取录取列表失败')
  }
}

const handleSearch = async () => {
  if (!searchKeyword.value.trim()) {
    fetchAdmissions()
    return
  }
  try {
    const response = await searchAdmissions(searchKeyword.value)
    if (response.data.success) {
      admissions.value = response.data.data
    }
  } catch (error) {
    ElMessage.error('搜索失败')
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增录取'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑录取'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该录取信息吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await deleteAdmission(row.id)
    if (response.data.success) {
      ElMessage.success('删除成功')
      fetchAdmissions()
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
          response = await updateAdmission(currentId.value, data)
        } else {
          response = await createAdmission(data)
        }

        if (response.data.success) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          fetchAdmissions()
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
    applicationId: null,
    admissionYear: '',
    admittedSchool: '',
    admittedMajor: '',
    admissionType: '全日制',
    supervisor: '',
    admissionScore: null,
    totalScore: null,
    status: '已录取',
    enrollmentDate: '',
    tuitionStandard: null,
    scholarshipLevel: '无奖学金',
    remarks: ''
  })
}

onMounted(() => {
  fetchAdmissions()
})
</script>

<style scoped>
.admissions-page {
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
