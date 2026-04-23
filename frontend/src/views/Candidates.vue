<template>
  <div class="candidates-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>考生信息管理</span>
          <el-button type="primary" @click="handleAdd">新增考生</el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索考生姓名、身份证号、手机号"
          style="width: 300px;"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button icon="Search" @click="handleSearch" />
          </template>
        </el-input>
      </div>

      <el-table :data="candidates" stripe border style="margin-top: 20px;">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="idCard" label="身份证号" width="180" />
        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="graduateSchool" label="毕业学校" width="150" />
        <el-table-column prop="graduateMajor" label="毕业专业" width="120" />
        <el-table-column prop="politicalStatus" label="政治面貌" width="100" />
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
      width="600px"
      @close="handleDialogClose"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthDate">
          <el-input v-model="form.birthDate" placeholder="如: 1998-01-01" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="毕业学校" prop="graduateSchool">
          <el-input v-model="form.graduateSchool" />
        </el-form-item>
        <el-form-item label="毕业专业" prop="graduateMajor">
          <el-input v-model="form.graduateMajor" />
        </el-form-item>
        <el-form-item label="毕业年份" prop="graduationYear">
          <el-input v-model="form.graduationYear" />
        </el-form-item>
        <el-form-item label="政治面貌" prop="politicalStatus">
          <el-input v-model="form.politicalStatus" />
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
  getCandidates,
  createCandidate,
  updateCandidate,
  deleteCandidate,
  searchCandidates
} from '../api/candidate'

const candidates = ref([])
const searchKeyword = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref(null)
const currentId = ref(null)

const form = reactive({
  name: '',
  idCard: '',
  gender: '男',
  birthDate: '',
  phone: '',
  email: '',
  address: '',
  graduateSchool: '',
  graduateMajor: '',
  graduationYear: '',
  politicalStatus: '',
  remarks: ''
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /^\d{17}[\dXx]$/, message: '身份证号格式不正确', trigger: 'blur' }
  ],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }]
}

const fetchCandidates = async () => {
  try {
    const response = await getCandidates()
    if (response.data.success) {
      candidates.value = response.data.data
    }
  } catch (error) {
    ElMessage.error('获取考生列表失败')
  }
}

const handleSearch = async () => {
  if (!searchKeyword.value.trim()) {
    fetchCandidates()
    return
  }
  try {
    const response = await searchCandidates(searchKeyword.value)
    if (response.data.success) {
      candidates.value = response.data.data
    }
  } catch (error) {
    ElMessage.error('搜索失败')
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增考生'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑考生'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该考生信息吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await deleteCandidate(row.id)
    if (response.data.success) {
      ElMessage.success('删除成功')
      fetchCandidates()
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
          response = await updateCandidate(currentId.value, data)
        } else {
          response = await createCandidate(data)
        }

        if (response.data.success) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          fetchCandidates()
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
    name: '',
    idCard: '',
    gender: '男',
    birthDate: '',
    phone: '',
    email: '',
    address: '',
    graduateSchool: '',
    graduateMajor: '',
    graduationYear: '',
    politicalStatus: '',
    remarks: ''
  })
}

onMounted(() => {
  fetchCandidates()
})
</script>

<style scoped>
.candidates-page {
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
