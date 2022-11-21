<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="企业名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入企业名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button
          :disabled="single"
          type="success"
          icon="el-icon-refresh"
          size="mini"
          v-hasPermi="['enterprise:enterprise:approve']"
          @click="resetparam('0')"
        >批量通过</el-button>
        <!-- <el-button
          :disabled="single"
          type="danger"
          icon="el-icon-refresh"
          size="mini"
          v-hasPermi="['enterprise:enterprise:approve']"
          @click="resetparam('1')"
        >批量驳回</el-button>-->
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="enterpriseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        width="300"
        label="企业名称"
        align="left"
        prop="name"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis;color: #44a5ff;"
            @click="handleRelease(scope.row)"
          >{{ scope.row.name }}</div>
        </template>
      </el-table-column>
      <el-table-column label="企业Logo" align="center" width="180" prop="logo">
        <template slot-scope="scope">
          <div v-if="scope.row.logo !== null">
            <el-image
              :preview-src-list="scope.row.logo"
              class="tablePic"
              :src="scope.row.logo[0]"
              alt
            />
          </div>
          <div v-else>
            <el-image :preview-src-list="morentup" class="tablePic" :src="morentup[0]" alt />
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="统一信用代码"
        :show-overflow-tooltip="true"
        align="center"
        width="300"
        prop="creditCode"
      />
      <el-table-column
        label="企业性质"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="property"
      />
      <el-table-column
        label="所属行业"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="industryName"
      />
      <el-table-column
        label="企业类型"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="type"
      />
      <el-table-column
        label="法人"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="legalPerson"
      />
      <el-table-column
        label="联系方式"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="legalPersonPhone"
      />
      <el-table-column
        label="所在地区"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="cityName"
      />
      <el-table-column
        label="注册资金(元)"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="registerFund"
      />
      <el-table-column label="成立时间" align="center" width="180" prop="establishTime"></el-table-column>
      <el-table-column label="浏览量" align="center" width="120" prop="views" />
      <el-table-column label="收藏量" align="center" width="120" prop="collects" />
      <el-table-column label="点赞量" align="center" width="120" prop="favorites" />
      <el-table-column label="首页推荐" align="center" width="120" prop="isRecommend">
        <template slot-scope="scope">{{
          scope.row.recommend === "0" ? "否" : "是"
        }}</template>
      </el-table-column>
      <el-table-column label="置顶" align="center" width="120" prop="isTop">
        <template slot-scope="scope">{{
          scope.row.isTop === "0" ? "否" : "是"
        }}</template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="120" prop="statusValue" />
      <el-table-column
        label="操作"
        align="center"
        fixed="right"
        width="200"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleRelease(scope.row)"
            v-hasPermi="['enterprise:enterprise:query']"
          >预览</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleUpdate(scope.row, 'ton')"
            v-hasPermi="['enterprise:enterprise:approve']"
          >审核通过</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleUpdate(scope.row, 'tui')"
            v-hasPermi="['enterprise:enterprise:approve']"
          >审核不通过</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改企业基础信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="900px"
      append-to-body
      class="dialog"
    >
      <div style="height: 70vh;overflow:auto;padding: 30px">
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="企业名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入企业名称" :disabled="reading" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="统一信用代码" prop="creditCode">
                <el-input v-model="form.creditCode" placeholder="请输入统一信用代码" :disabled="reading" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="注册资金" prop="registerFund">
                <el-input v-model="form.registerFund" placeholder="请输入注册资金" :disabled="reading" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="企业官网" prop="officialWebsite">
                <el-input v-model="form.officialWebsite" placeholder="请输入企业官网" :disabled="reading" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="企业性质" prop="property">
                <el-select v-model="form.property" placeholder="请选择企业性质" :disabled="reading">
                  <el-option
                    v-for="item in natureOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属行业" prop="industryId">
                <TreeSelecter
                  v-if="!reading"
                  :props="treeSelectProps"
                  v-model="form.industryId"
                  :options="treeSelectOptions"
                  :value="form.industryId"
                ></TreeSelecter>
                <div v-if="reading">{{ form.industryName }}</div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="企业类型" prop="type">
                <el-select v-model="form.type" placeholder="请选择企业类型" :disabled="reading">
                  <el-option
                    v-for="item in businessTypeOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="企业法人" prop="legalPerson">
                <el-input v-model="form.legalPerson" placeholder="请输入企业法人" :disabled="reading" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="legalPersonPhone">
                <el-input
                  v-model="form.legalPersonPhone"
                  placeholder="请输入联系电话"
                  :disabled="reading"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="法人代表" prop="legalRepresent">
                <el-input v-model="form.legalRepresent" placeholder="请输入法人代表" :disabled="reading" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="legalRepresentPhone">
                <el-input
                  v-model="form.legalRepresentPhone"
                  placeholder="请输入联系电话"
                  :disabled="reading"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="员工人数" prop="staffNumber">
                <el-input v-model="form.staffNumber" placeholder="请输入员工人数" :disabled="reading" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="成立时间" prop="establishTime">
                <el-date-picker
                  :disabled="reading"
                  v-model="form.establishTime"
                  type="date"
                  placeholder="选择日期"
                ></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col style="display: flex" :span="24">
              <el-form-item label="所在地区" prop="province">
                <el-select
                  v-model="form.province"
                  @change="provinceChange"
                  placeholder="请选择省"
                  :disabled="reading"
                >
                  <el-option
                    v-for="item in provinceOptions"
                    :key="item.regionCode"
                    :label="item.regionName"
                    :value="item.regionCode"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="city">
                <el-select
                  v-model="form.city"
                  @change="cityChange"
                  placeholder="请选择市"
                  :disabled="reading"
                >
                  <el-option
                    v-for="item in cityOptions"
                    :key="item.regionCode"
                    :label="item.regionName"
                    :value="item.regionCode"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="region">
                <el-select v-model="form.region" placeholder="请选择区" :disabled="reading">
                  <el-option
                    v-for="item in regionOptions"
                    :key="item.regionCode"
                    :label="item.regionName"
                    :value="item.regionCode"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="经营范围" prop="businessScope">
                <el-input
                  type="textarea"
                  :rows="2"
                  placeholder="请输入内容"
                  :disabled="reading"
                  v-model="form.businessScope"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="主营产品" prop="mainProduct">
                <el-input
                  type="textarea"
                  :rows="2"
                  placeholder="请输入内容"
                  :disabled="reading"
                  v-model="form.mainProduct"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="企业Logo" prop="logo">
                <upLoadPic
                  v-if="!reading"
                  :cover="form.logo"
                  @handleFileSuccess="handleFileSuccess1"
                  @handleRemove="handleRemove1"
                ></upLoadPic>
                <img class="tablePic" :src="form.logo" alt v-if="reading" />
                <div v-if="!reading">
                  <span>建议：上传图片尺寸为276*155，图片格式支持png、jpg、GIF、JPEG</span>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="营业执照" prop="license">
                <upLoadPic
                  v-if="!reading"
                  :cover="form.license"
                  @handleFileSuccess="handleFileSuccess2"
                  @handleRemove="handleRemove2"
                ></upLoadPic>
                <img class="tablePic" :src="form.license" alt v-if="reading" />
                <div v-if="!reading">
                  <span>建议：上传图片尺寸为276*155，图片格式支持png、jpg、GIF、JPEG</span>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="企业描述" prop="describing">
                <div
                  v-if="reading"
                  class="ql-editor"
                  v-html="form.describing"
                  style="border:1px solid #ccc"
                ></div>
                <editor v-model="form.license" :min-height="192" v-if="!reading" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="首页推荐" prop="isRecommend">
                <el-switch
                  :disabled="reading"
                  active-value="1"
                  inactive-value="0"
                  v-model="form.isRecommend"
                ></el-switch>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="置顶" prop="isTop">
                <el-switch
                  :disabled="reading"
                  active-value="1"
                  inactive-value="0"
                  v-model="form.isTop"
                ></el-switch>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="opentwo"
      width="500px"
      append-to-body
    >
      <el-row :gutter="20">
        <el-form :span="12">
          <el-form-item label="驳回原因">
            <el-input v-model="queryParams.bohui" type="textarea" placeholder="请输入驳回原因" />
          </el-form-item>
        </el-form>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="canceltwo">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listEnterprise,
  getEnterprise,
  examineEnterprise,
  getRegion,
} from '@/api/enterprise/examine/examine'
import upLoadPic from '../../../components/upLoadPic'
import TreeSelecter from '../../../components/treeSelect'
import {
  getIndustryType,
  getIndustryNature,
} from '@/api/enterprise/enterprise.js'

export default {
  name: 'Enterprise',
  components: { TreeSelecter, upLoadPic },
  data() {
    return {
      morentup: [require('@/assets/images/profile.jpg')],
      opentwo: false,
      reading: true,
      cityOptions: [],
      regionOptions: [],
      provinceOptions: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      idee: '',
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 企业基础信息表格数据
      enterpriseList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      showThreeInput: true,
      treeSelectProps: {
        value: 'id', // ID字段名
        label: 'typeName', // 显示名称
        // children: 'children'    // 子级字段名
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        industryId: null,
        creditCode: null,
        name: null,
        englishName: null,
        property: null,
        type: null,
        legalPerson: null,
        legalPersonPhone: null,
        legalRepresent: null,
        legalRepresentPhone: null,
        province: null,
        city: null,
        region: null,
        address: null,
        businessScope: null,
        officialWebsite: null,
        mainProduct: null,
        staffNumber: null,
        establishTime: null,
        registerFund: null,
        isTop: null,
        isSupplier: null,
        status: null,
        approveBy: null,
        approveTime: null,
        enterpriseService: null,
        views: null,
        aastatused: '',
        collects: null,
        favorites: null,
        userId: null,
        deptId: null,
        bohui: '',
      },
      businessTypeOptions: [],
      natureOptions: [],
      industryOptions: [],
      areaOptions: [
        {
          value: '0',
          label: '北京市',
        },
        {
          value: '1',
          label: '石景山区',
        },
        {
          value: '2',
          label: '石景山',
        },
      ],
      // 表单参数
      form: {
        name: '', //企业名称
        creditCode: '', //统一信用代码
        registerFund: null, //注册资金
        officialWebsite: '', //企业官网
        property: null, //企业性质
        industryId: null, //所属行业
        type: null, //企业类型
        legalPerson: '', //企业法人,
        legalPersonPhone: '', //联系方式
        legalRepresent: '', //法人代表
        legalRepresentPhone: '', //代表电话
        staffNumber: null, //员工人数
        establishTime: null, //成立时间
        province: null, //省
        city: null, //市
        region: null, //区
        businessScope: '', //经营范围
        mainProduct: '', //主营产品
        logo: null, //logo
        license: null, //营业执照
        describing: null, //企业描述
        isRecommend: false, //首页推荐
        isTop: false, //置顶
        views: null, //浏览量
        collects: null, //收藏量
        favorites: null, //点赞量
        businessTypeOptions: [],
        natureOptions: [],
      },
      treeSelectOptions: [],
      // 表单校验
      rules: {},
    }
  },
  created() {
    this.getList()
    this.getIndustryNature()
    // this.getIndustryTree();
    this.getIndustryType()
    this.cityChange()
  },
  methods: {
    // 获取企业性质
    getIndustryNature() {
      getIndustryNature().then((res) => {
        this.natureOptions = res.data
      })
    },
    // 获取企业类型
    getIndustryType() {
      getIndustryType().then((res) => {
        this.businessTypeOptions = res.data
      })
    },
    /** 查询待审核企业基础信息列表 */
    getList() {
      this.loading = true
      listEnterprise(this.queryParams).then((response) => {
        this.enterpriseList = response.rows
        this.enterpriseList.forEach((element) => {
          if (element.logo !== null) {
            element.logo = element.logo.split(',')
          }
        })
        this.total = response.total
        this.loading = false
      })
    },
    getProvince() {
      getRegion(0).then((res) => {
        this.provinceOptions = res.data
      })
    },
    provinceChange() {
      getRegion(this.form.province).then((res) => {
        this.cityOptions = res.data
        this.form.city = null
        this.form.region = null
      })
    },
    resetparam(val) {
      const add = {
        ids: this.ids,
        status: val,
      }
      this.$modal
        .confirm('是否确认操作')
        .then(function () {
          return examineEnterprise(add)
        })
        .then(() => {
          this.$modal.msgSuccess('审核成功')
          this.getList()
        })
    },
    cityChange() {
      getRegion(this.form.city).then((res) => {
        this.regionOptions = res.data
        this.form.region = null
      })
    },

    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        name: '', //企业名称
        creditCode: '', //统一信用代码
        registerFund: null, //注册资金
        officialWebsite: '', //企业官网
        property: null, //企业性质
        industryId: null, //所属行业
        type: null, //企业类型
        legalPerson: '', //企业法人,
        legalPersonPhone: '', //联系方式
        legalRepresent: '', //法人代表
        legalRepresentPhone: '', //代表电话
        staffNumber: null, //员工人数
        establishTime: null, //成立时间
        province: null, //省
        city: null, //市
        region: null, //区
        businessScope: '', //经营范围
        mainProduct: '', //主营产品
        logo: null, //logo
        license: null, //营业执照
        describing: null, //企业描述
        isRecommend: false, //首页推荐
        isTop: false, //置顶
        views: null, //浏览量
        collects: null, //收藏量
        favorites: null, //点赞量
      }
      this.resetForm('form')
    },
    handleFileSuccess(file) {
      this.form.cover = file.src
    },
    // 图片删除
    handleRemove() {
      this.form.cover = ''
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
      if (selection.length !== '0') {
        this.single = false
      }
      this.multiple = !selection.length
    },
    // 驳回弹框取消按钮
    canceltwo() {
      this.opentwo = false
      this.reset()
    },
    /** 审核按钮操作 */
    handleUpdate(row, val) {
      if (val == 'ton') {
        this.aastatused = 0
      } else if (val == 'tui') {
        this.title = '驳回'
        this.aastatused = 1
        this.idee = row.id
        return (this.opentwo = true)
      }
      this.reset()
      const add = {
        ids: [row.id],
        remark: '',
        status: this.aastatused,
      }
      this.$modal
        .confirm('是否确认操作')
        .then(function () {
          return examineEnterprise(add)
        })
        .then(() => {
          if (add.status == 0) {
            this.$modal.msgSuccess('发布成功')
          } else {
            this.$modal.msgSuccess('退回成功')
          }
          this.getList()
        })
    },
    // 审核提交
    submit() {
      const add = {
        status: 1,
        remark: this.queryParams.bohui,
        ids: [this.idee],
      }
      examineEnterprise(add).then(() => {
        this.$modal.msgSuccess('审核成功')
        this.opentwo = false
        this.queryParams.radio = ''
        this.queryParams.bohui = ''
        this.getList()
      })
    },
    // 预览按钮操作
    handleRelease(row) {
      const id = row.id || this.ids
      getEnterprise(id).then((response) => {
        this.open = true
        this.form = response.data
        this.title = '预览'
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.tablePic {
  height: 48px;
  width: 72px;
}
.dialog ::v-deep .el-dialog__body {
  padding: 0;
}
</style>
