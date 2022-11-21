<template>
  <div class="container">
    <div class="info">
      <div class="view-title">
        <span>个人信息</span>
      </div>
      <el-form :model="form" size="small" label-width="128px">
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" disabled />
        </el-form-item>
        <el-form-item label="任职企业" prop="enterpriseName">
          <el-input v-model="form.enterpriseName" disabled />
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" disabled />
        </el-form-item>
        <el-form-item label="手机号" prop="phonenumber">
          <el-input v-model="form.phonenumber" disabled />
        </el-form-item>
        <el-form-item label="院校" prop="college">
          <el-input v-model="form.college" disabled />
        </el-form-item>
        <el-form-item label="研究领域" prop="research">
          <el-input v-model="form.research" disabled />
        </el-form-item>
        <el-form-item label="联系方式" prop="phonenumber">
          <el-input v-model="form.phonenumber" disabled />
        </el-form-item>
        <el-form-item label="学历" prop="education">
          <el-input v-model="form.education" disabled />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
export default {
  name: 'businessPreview',
  props: {
    form: {
      //数据
      type: [Object],
      required: false,
      default: function () {
        return {}
      },
    },
  },
}
</script>
<style lang="scss" scoped>
.container {
  .view-title {
    font-size: 20px;
    margin-bottom: 20px;
    &::before {
      content: '';
      display: inline-block;
      background-color: #3eacef;
      height: 24px;
      width: 4px;
      margin-right: 40px;
      vertical-align: -2px;
    }
  }
}
</style>
