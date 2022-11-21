<template>
  <div class="container">
    <el-form :model="form" size="small" label-width="128px">
      <div class="info">
        <div class="view-title">
          <span>企业信息</span>
        </div>
        <el-form-item label="企业名称" prop="name">
          <el-input v-model="form.name" disabled />
        </el-form-item>
        <el-form-item label="统一信用代码" prop="creditCode">
          <el-input v-model="form.creditCode" disabled />
        </el-form-item>
        <el-form-item label="所属行业" prop="industryName">
          <el-input v-model="form.industryName" disabled />
        </el-form-item>
        <el-form-item label="企业性质" prop="property">
          <el-input v-model="form.property" disabled />
        </el-form-item>
        <el-form-item label="企业类型" prop="type">
          <el-checkbox-group v-model="selectType">
            <el-checkbox
              :label="item.value"
              disabled
              v-for="item in dict.type.enterprise_type"
              :key="item.value"
            >{{item.label}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="法人" prop="legalPerson">
          <el-input v-model="form.legalPerson" disabled />
        </el-form-item>
        <el-form-item label="法人联系方式" prop="legalPersonPhone">
          <el-input v-model="form.legalPersonPhone" disabled />
        </el-form-item>
        <el-form-item label="法人代表" prop="legalRepresent">
          <el-input v-model="form.legalRepresent" disabled />
        </el-form-item>
        <el-form-item label="法人代表联系方式" prop="legalRepresentPhone">
          <el-input v-model="form.legalRepresentPhone" disabled />
        </el-form-item>
        <el-row :gutter="20" class="mb8">
          <el-col :span="10">
            <el-form-item label="所在地区" prop="provinceName">
              <el-input v-model="form.provinceName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item prop="cityName" label-width="0">
              <el-input v-model="form.cityName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item prop="regionName" label-width="0">
              <el-input v-model="form.regionName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="经营范围" prop="businessScope">
          <el-input v-model="form.businessScope" disabled />
        </el-form-item>
        <el-form-item label="公司官网" prop="officialWebsite">
          <el-input v-model="form.officialWebsite" disabled />
        </el-form-item>
        <el-form-item label="主营产品" prop="mainProduct">
          <el-input v-model="form.mainProduct" disabled />
        </el-form-item>
        <el-form-item label="员工人数" prop="staffNumber">
          <el-input v-model="form.staffNumber" disabled />
        </el-form-item>
        <el-form-item label="成立时间" prop="establishTime">
          <el-input v-model="form.establishTime" disabled />
        </el-form-item>
        <el-form-item label="注册资金" prop="registerFund">
          <el-input v-model="form.registerFund" disabled />
        </el-form-item>
        <el-form-item label="企业描述" prop="describing">
          <div v-html="form.describing" class="describing"></div>
        </el-form-item>
      </div>
      <div class="papers">
        <div class="view-title">
          <span>企业证件</span>
        </div>
        <div class="imgs-container">
          <div class="logo">
            <div class="img-title">企业logo</div>
            <el-image
              style="width: 300px; height: 200px;background-color: #eee"
              :src="form.logo"
              fit="contain"
            ></el-image>
          </div>
          <div class="license">
            <div class="img-title">营业执照</div>
            <el-image style="width: 300px;background-color: #eee" :src="form.license" fit="contain"></el-image>
          </div>
        </div>
      </div>
      <div>
        <div class="view-title">
          <span>经营情况</span>
        </div>
        <el-form-item label="年营业额" prop="yearTurnover">
          <el-input v-model="form.yearTurnover" disabled />
        </el-form-item>
        <el-form-item label="平均毛利" prop="grossMargin">
          <el-input v-model="form.grossMargin" disabled />
        </el-form-item>
        <el-form-item label="坏账率" prop="badDebtRate">
          <el-input v-model="form.badDebtRate" disabled />
        </el-form-item>
        <el-form-item label="年利润" prop="yearProfit">
          <el-input v-model="form.yearProfit" disabled />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" disabled />
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>
<script>
export default {
  name: 'businessPreview',
  dicts: ['enterprise_type'],
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
  data() {
    return {
      selectType: this.form.type ? [this.form.type] : [],
    }
  },
}
</script>
<style lang="scss" scoped>
.describing {
  border: 1px solid #dfe4ed;
  background-color: #f5f7fa;
  color: #c0c4cc;
  cursor: not-allowed;
  border-radius: 4px;
  padding: 0 15px;
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
  .papers {
    margin-bottom: 22px;
    .imgs-container {
      display: flex;
      justify-content: space-between;
      padding-left: 60px;
      > div {
        flex: 1 0 50%;
        display: flex;
        .img-title {
          margin-right: 20px;
        }
      }
    }
  }
}
</style>
