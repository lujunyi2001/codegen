<template>
  <div class="app-container">
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane name="first">
        <span slot="label"><i class="el-icon-edit-outline"></i> 生成代码</span>
        <generate-config />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import GenerateConfig from './GenerateConfig'
export default {
  components: { GenerateConfig },
  data() {
    return {
      activeName: 'first'
    }
  }
}
</script>

