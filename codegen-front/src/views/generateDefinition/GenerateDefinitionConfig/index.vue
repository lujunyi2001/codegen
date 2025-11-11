<template>
  <div class="code-gen">
    <el-form ref="genForm" class="gen-form" :model="clientParam" size="mini" label-width="150px">
      <el-form-item label="选择数据源" prop="datasourceConfigId" :rules="{required: true, message: '请选择数据源'}">
        <el-select
          v-model="clientParam.datasourceConfigId"
          placeholder="选择数据源"
          @change="onDataSourceChange"
        >
          <el-option
            v-for="item in datasourceConfigList"
            :key="item.id"
            :label="`${item.username} （${item.host}:${item.port}/${item.dbName}）`"
            :value="item.id"
          >
            <span style="float: left">{{ `${item.username} （${item.host}:${item.port}/${item.dbName}）` }} </span>
          </el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item v-show="showTable" label="包名前缀（packagePrefix）">
        <el-input v-model="clientParam.packageNamePrefix" placeholder="可选，如：cn.studyjava.xxx" show-word-limit maxlength="100" />
      </el-form-item>-->
    </el-form>
    <el-row v-show="showTable" :gutter="32">
      <el-col id="definitionSelect">
        <el-button v-show="showTable" type="primary" @click="onDeleteTableDefinition">删除</el-button>
        <el-button v-show="showTable" type="primary" @click="showTemplateDialog = true">生成代码</el-button>
        <h4>选择表</h4>
        <el-input
          v-model="tableSearch"
          prefix-icon="el-icon-search"
          clearable
          placeholder="过滤表"
          style="margin-bottom: 10px;width: 100%;"
        />
        <el-table
          :data="tableListData"
          border
          style="width: 100%"
          :row-class-name="tableRowClassName"
          @selection-change="onTableListSelect"
        >
          <el-table-column
            type="selection"
          />
          <el-table-column
            prop="tableName"
            label="表名"
          />
          <el-table-column
            prop="comment"
            label="注释"
          />
          <el-table-column
            prop="moduleName"
            label="模块名"
          />
          <el-table-column
            prop="className"
            label="类名"
          />
          <el-table-column
            prop="packageName"
            label="包名"
          />
          <el-table-column
            fixed="right"
            label="操作"
            width="100">
            <template slot-scope="scope">
              <el-button @click="editTableDefinition(scope.row)" type="text" size="small">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>

    </el-row>

    <el-dialog
      :title="编辑表定义"
      :visible.sync="showTableDefinitionDialog"
      width="90%"
    >
      <h4>编辑表定义</h4>
      <el-tabs :tab-position="tabPosition">
        <el-tab-pane label="表定义">
          <el-form
            ref="tableDefinitionForm"
            :model="selectTableDefinition"
            :rules="tableDefinitionormRules"
            size="mini"
            label-width="120px"
          >
            <el-form-item prop="tableName" label="表名">
              <span>{{selectTableDefinition.tableName}}</span>
            </el-form-item>
              <el-form-item prop="comment" label="表注释">
                <el-input v-model="selectTableDefinition.comment" show-word-limit maxlength="60" />
              </el-form-item>
            <el-form-item prop="moduleName" label="模块名">
              <el-input v-model="selectTableDefinition.moduleName" show-word-limit maxlength="60" />
            </el-form-item>
            <el-form-item prop="className" label="类名">
              <el-input v-model="selectTableDefinition.className" show-word-limit maxlength="60" />
            </el-form-item>
              <el-form-item prop="packageName" label="子包名">
                <el-input v-model="selectTableDefinition.packageName" show-word-limit maxlength="100" />
              </el-form-item>
          </el-form>

        </el-tab-pane>
        <el-tab-pane label="列定义">
          <el-table :data="selectTableDefinition.columnDefinitions" style="width: 100%" height="600px">
            <el-table-column prop="columnName" label="字段名"></el-table-column>
            <el-table-column prop="type" label="数据库类型">
              <template #default="{row}">
                <span>{{ row.type + (row.numberPrecision > 0 || row.numberScale > 0?'(':'') +
                (row.numberPrecision > 0?row.numberPrecision:(row.numberScale > 0?'0':'')) +
                (row.numberScale > 0?(',' + row.numberScale):'') +
                ((row.numberPrecision > 0 || row.numberScale > 0?')':''))}}</span>
              </template>

            </el-table-column>
            <el-table-column prop="isPk" label="是否为主键" width="90">
              <template #default="{row}">
                <span>{{ row.isPk?'是':'' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="isIdentity" label="是否自增" width="80">
              <template #default="{row}">
                <span>{{ row.isIdentity?'是':'' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="comment" label="字段注释">
              <template #default="{row}">
                <el-input v-model="row.comment"></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="fieldName" label="成员变量名称">
              <template #default="{row}">
                <el-input v-model="row.fieldName"></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="fieldType" label="成员变量类型">
              <template #default="{row}">
                <el-input v-model="row.fieldType"></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="canInsert" label="插入" width="40">
              <template #default="{row}">
                <el-checkbox v-model="row.canInsert"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column prop="canEdit" label="编辑" width="40">
              <template #default="{row}">
                <el-checkbox v-model="row.canEdit"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column prop="isInList" label="列表" width="40">
              <template #default="{row}">
                <el-checkbox v-model="row.inList"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column prop="canQuery" label="查询" width="40">
              <template #default="{row}">
                <el-checkbox v-model="row.canQuery"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column prop="queryType" label="查询类型">
              <template #default="{row}">
                <el-select
                  v-model="row.queryType"
                >
                  <el-option
                    v-for="item in queryTypeData"
                    :key="item.code"
                    :label="item.name"
                    :value="item.code"
                  >
                    <span style="float: left">{{ item.name }} </span>
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="widgetType" label="控件类型">
              <template #default="{row}">
                <el-select
                  v-model="row.widgetType"
                >
                  <el-option
                    v-for="item in widgetTypeData"
                    :key="item.code"
                    :label="item.name"
                    :value="item.code"
                  >
                    <span style="float: left">{{ item.name }} </span>
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="widgetLabel" label="显示名称">
              <template #default="{row}">
                <el-input v-model="row.widgetLabel"></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="dictName" label="字典名称">
              <template #default="{row}">
                <el-input v-model="row.dictName"></el-input>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showTableDefinitionDialog = false">取 消</el-button>
        <el-button type="primary" @click="saveTableDefinition">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      :title="选择模板"
      :visible.sync="showTemplateDialog"
    >
      <el-form ref="genForm" class="gen-form" :model="clientParam" size="mini" label-width="200px" label-position="left">
        <el-form-item v-show="showTable" label="包名前缀（packagePrefix）">
          <el-input v-model="clientParam.packageNamePrefix" placeholder="可选，如：cn.studyjava.xxx" show-word-limit maxlength="100" />
        </el-form-item>
      </el-form>

      <h4>选择模板</h4>
      <el-select
        v-model="clientParam.groupName"
        placeholder="选择模板所在组"
        size="mini"
        style="margin-bottom: 10px; width: 100%;"
        @change="onDataGroupChange"
      >
        <el-option
          v-for="item in groupData"
          :key="item.id"
          :label="`${item.groupName}`"
          :value="item.id"
        />
      </el-select>
      <el-table
        :data="templateListData"
        border
        :row-class-name="templateTableRowClassName"
        @selection-change="onTemplateListSelect"
      >
        <el-table-column
          type="selection"
        />
        <el-table-column
          prop="name"
          label="模板名称"
        >
          <span slot-scope="scope">
            <!--              {{scope.row.groupName}}-{{scope.row.name}}-->
            {{ scope.row.name }}
          </span>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showTemplateDialog = false">取 消</el-button>
        <el-button type="primary" @click="onGenerate">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<style lang="scss">
  .code-gen {
    margin: 0 auto;
    width: 70%;
      .el-input { width: 450px;}
      .el-row h4 {
        text-align: center;
      }
      .el-row .el-button {
        margin-top: 20px;
      }
  }
  .el-table .hidden-row {
    display: none;
  }
  #definitionSelect {
    .el-input { width: 100%;}
  }
</style>
<script>
export default {
  name: 'GenerateConfig',
  data() {
    return {
      groupId: '',
      groupData: {},
      showTable: false,
      selectDatasourceId: null,
      clientParam: {
        datasourceConfigId: '',
        tableNames: [],
        tableDefinitionIds: [],
        templateConfigIdList: [],
        packageNamePrefix: null,
        delPrefix: null,
        groupId: '',
        groupName: ''
      },
      tableSearch: '',
      datasourceConfigList: [],
      queryTypeData: [],
      widgetTypeData: [],
      tableListData: [],
      templateListData: [],
      selectTableDefinition: {},
      showTableDefinitionDialog: false,
      showTemplateDialog: false,
      tableDefinitionormRules: {
        comment: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9]+$/, message: '只能包含字母数字', trigger: 'blur' },
          { max: 60, message: '长度不能大于个字符', trigger: 'blur' }
        ],
        moduleName: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9]+$/, message: '只能包含字母数字', trigger: 'blur' },
          { max: 60, message: '长度不能大于个字符', trigger: 'blur' }
        ],
        className: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9]+$/, message: '只能包含字母数字', trigger: 'blur' },
          { max: 60, message: '长度不能大于个字符', trigger: 'blur' }
        ],
        packageName: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9]+$/, message: '只能包含字母数字', trigger: 'blur' },
          { max: 60, message: '长度不能大于个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.clientParam.datasourceConfigId = this.$route.params.datasourceId || ''
    if (this.clientParam.datasourceConfigId.startsWith(':')) {
      this.clientParam.datasourceConfigId = null
    }
    this.loadDataSource()
    this.loadTemplate()
    this.loadGroups()
    this.loadQueryType()
    this.loadWidgetType()
  },
  methods: {
    tableRowClassName: function({ row, index }) {
      // console.log("tablerow="+row.tableName+","+index)
      row.hidden = false
      if (this.tableSearch.length === 0) {
        return ''
      }
      // console.log("tablerow="+row.tableName +","+ row.tableName.indexOf(this.tableName)+","+(!(row.tableName && row.tableName.indexOf(this.tableSearch) > -1)))
      if (!(row.tableName && row.tableName.toLowerCase().indexOf(this.tableSearch.toLowerCase()) > -1)) {
        row.hidden = true
        return 'hidden-row'
      }
      return ''
    },
    templateTableRowClassName: function({ row, index }) {
      // console.log("temprow="+row.id+",rowGroupId="+row.groupId+", this.groupId="+ this.groupId)
      row.hidden = false
      if (this.groupId === '' || this.groupId <= 0) {
        return ''
      }
      // console.log("temprow="+row.groupId +","+(row.groupId && row.groupId == this.groupId))
      if (row.groupId && row.groupId === this.groupId) {
        return ''
      }
      row.hidden = true
      return 'hidden-row'
    },
    loadGroups() {
      this.post(`/group/list/`, {}, function(resp) {
        this.groupData = resp.data
      })
    },
    loadDataSource() {
      this.post('/datasource/list', {}, resp => {
        this.datasourceConfigList = resp.data
        if (this.clientParam.datasourceConfigId) {
          this.onDataSourceChange(this.clientParam.datasourceConfigId)
        }
      })
    },
    loadTemplate() {
      this.post('/template/list', {}, resp => {
        this.templateListData = resp.data
      })
    },
    loadQueryType() {
      this.post('/queryType/list', {}, resp => {
        this.queryTypeData = resp.data
      })
    },
    loadWidgetType() {
      this.post('/widget/list', {}, resp => {
        this.widgetTypeData = resp.data
      })
    },
    onTableListSelect(selectedRows) {
      this.clientParam.tableDefinitionIds = selectedRows
        .filter(row => row.hidden === undefined || row.hidden === false)
        .map(row => row.id)
    },
    onTemplateListSelect(selectedRows) {
      this.clientParam.templateConfigIdList = selectedRows
        .filter(row => row.hidden === undefined || row.hidden === false)
        .map(row => row.id)
    },
    onDataSourceChange(datasourceConfigId) {
      this.clientParam.datasourceConfigId = datasourceConfigId
      this.post(`/definition/query`, { 'datasourceConfigId': datasourceConfigId }, resp => {
        this.showTable = true
        this.tableListData = resp.data
      })
    },
    editTableDefinition(tableDefinition) {
      this.post(`/definition/table/` + tableDefinition.id, {}, resp => {
        console.log(resp)
        this.selectTableDefinition = resp.data
        this.showTableDefinitionDialog = true
      })
    },
    saveTableDefinition(tableDefinition) {
      this.post(`/definition/save`, this.selectTableDefinition, resp => {
        this.tip('保存成功')
        this.showTableDefinitionDialog = false
      })

    },
    onDeleteTableDefinition(){
      if (this.clientParam.tableDefinitionIds.length === 0) {
        this.tip('请勾选表', 'error')
        return
      }
      this.confirm(`确认要删除？`, function(done) {
        this.post(`/definition/delBatch`, this.clientParam, resp => {
          this.tip('删除成功')
          this.onDataSourceChange(this.clientParam.datasourceConfigId)
        })
        done()
      })
    },
    onGenerate() {
      this.$refs.genForm.validate((valid) => {
        if (valid) {
          if (this.clientParam.tableDefinitionIds === 0) {
            this.tip('请勾选表', 'error')
            return
          }
          this.showTemplateDialog = true
          if (this.clientParam.templateConfigIdList.length === 0) {
            this.tip('请勾选模板', 'error')
            return
          }
          const config = JSON.stringify(this.clientParam)
          this.goRoute(`../result/${config}`)
        }

      })
    }
  }
}
</script>

