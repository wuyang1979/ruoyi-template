<template>
  <div class="container">
    <div class="info">
      <div class="view-title">
        <span>个人信息</span>
      </div>
      <el-form :model="form" size="small" label-width="128px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="政治面貌" prop="political">
              <el-input v-model="politicsName" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="研究方向" prop="researchDirection">
              <el-input v-model="form.researchDirection" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="院校" prop="college">
              <el-input v-model="form.college" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学历" prop="education">
              <el-input v-model="educationName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="籍贯" prop="nativePlace">
              <el-input v-model="form.nativePlace" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="证件类型" prop="certificateType">
              <el-input v-model="certificateTypeName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="证件号码" prop="certificateNumber">
              <el-input v-model="form.certificateNumber" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="简介" prop="introduction">
          <el-input v-model="form.introduction" type="textarea" disabled />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <div
            style="padding: 5px 15px;border:1px solid #dfe4ed;background: #F5F7FA;color: #C0C4CC;border-radius: 4px;min-height: 50px;"
            v-html="form.content"
          ></div>
        </el-form-item>
        <el-form-item label="专家照片" prop="expertPic">
          <el-image
            style="width: 120px; height: 160px;background-color: #eee"
            :src="form.expertPic"
            fit="contain"
          ></el-image>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input disabled v-model="form.remark" type="textarea" />
        </el-form-item>
        <el-form-item label="资质证书" prop="certificatePic"></el-form-item>
        <el-row v-if="form.credentials" :gutter="20">
          <div
            v-for="(item, index) in form.credentials"
            :key="index"
            style="
                border: 1px solid #ccc;
                width: 250px;
                display: inline-block;
                margin-left: 35px;
                margin-top: 20px;
              "
          >
            <img class="tablePicee" :src="item.url" alt />

            <el-form-item label="发证单位:" label-width="120px">
              <span>{{ item.issuedBy }}</span>
            </el-form-item>
            <el-form-item label="证书编号:" label-width="120px">
              <span>{{ item.number }}</span>
            </el-form-item>
            <el-form-item label-width="120px">
              <label slot="label">有&nbsp; 效&nbsp; 期:</label>
              <span>{{ item.validityTime }}</span>
            </el-form-item>
          </div>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
export default {
  name: 'businessPreview',
  dicts: ['xy_expert_politics', 'xy_expert_education', 'xy_expert_certificate'],
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
  computed: {
    politicsName() {
      let str = this.dict.type.xy_expert_politics.find((ele) => {
        return ele.value == this.form.politicsStatus
      })
      if (str) {
        return str.label
      } else {
        return ''
      }
    },
    educationName() {
      let str = this.dict.type.xy_expert_education.find((ele) => {
        return ele.value == this.form.education
      })
      if (str) {
        return str.label
      } else {
        return ''
      }
    },
    certificateTypeName() {
      let str = this.dict.type.xy_expert_certificate.find((ele) => {
        return ele.value == this.form.certificateType
      })
      if (str) {
        return str.label
      } else {
        return ''
      }
    },
  },
}
</script>
<style lang="scss" scoped>
.tablePicee {
  width: 144px;
  height: 96px;
  display: block;
  margin: 20px auto;
}
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
