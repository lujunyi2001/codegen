<template>
  <div class="app-container">
    <el-button type="primary" size="mini" icon="el-icon-plus" style="margin-bottom: 10px;" @click="onTableAdd">新增查询类型</el-button>
    <el-table
      :data="tableData"
      border
      highlight-current-row
    >
      <el-table-column
        prop="code"
        label="查询类型代码"
      />
      <el-table-column
        prop="name"
        label="查询类型名称"
      />
      <el-table-column
        label="操作"
      >
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="onTableUpdate(scope.row)">修改</el-button>
          <el-button type="text" size="mini" @click="onTableDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      :title="queryTypeTitle"
      :visible.sync="queryTypeDlgShow"
    >
      <el-form
        ref="dialogForm"
        :model="formData"
        :rules="formRules"
        size="mini"
        label-width="120px"
      >
        <el-form-item prop="name" label="查询类型代码">
          <el-input v-model="formData.code" show-word-limit maxlength="100" />
        </el-form-item>
        <el-form-item prop="name" label="查询类型名称">
          <el-input v-model="formData.name" show-word-limit maxlength="100" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSave">保 存</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      queryTypeTitle: '',
      queryTypeDlgShow: false,
      formData: {
        id: 0,
        code: '', 
        name: ""
      },
      formRules: {
        code: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadTable()
  },
  methods: {
    loadTable: function() {
      this.post('/queryType/list', {}, function(resp) {
        this.tableData = resp.data
      })
    },
    onTableAdd: function(row) {
      this.queryTypeTitle = '增加模板组'
      this.formData = {
        id: null,
        code: '',
        name: ""
      }
      this.queryTypeDlgShow = true
    },
    onTableUpdate: function(row) {
      this.queryTypeTitle = '修改查询类型'
      this.post(`/queryType/get/${row.id}`, {}, function(resp) {
        this.formData = resp.data
      })
      this.queryTypeDlgShow = true
    },
    onTableDelete: function(row) {
      this.confirm(`确认要删除【${row.name}】吗？`, function(done) {
        this.post('/queryType/del', row, function() {
          done()
          this.tip('删除成功')
          this.loadTable()
        })
      })
    },
    onAdd: function() {
      this.goRoute('edit/0')
    },
    onSave() {
      this.$refs.dialogForm.validate((valid) => {
        if (valid) {
          console.log(this.formData.id)
          const uri = `/queryType/save`
          this.post(uri, this.formData, resp => {
            this.formData.id = resp.data.id
            this.tip('保存成功')
            this.loadTable()
            this.queryTypeDlgShow = false
          })
        }
      })
    }
  }
}
</script>
