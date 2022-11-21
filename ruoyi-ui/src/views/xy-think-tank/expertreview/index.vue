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
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="院校" prop="college">
        <el-input
          v-model="queryParams.college"
          placeholder="请输入院校"
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
          @click="resetparam('0')"
          v-hasPermi="['thinkTank:expert:batch']"
        >批量通过</el-button>
        <!-- <el-button
          :disabled="single"
          type="danger"
          icon="el-icon-refresh"
          size="mini"
          @click="resetparam('1')"
          v-hasPermi="['thinkTank:expert:batch']"
        >批量驳回</el-button> -->
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="newsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :show-overflow-tooltip="true" />

      <el-table-column
        label="姓名"
        width="120"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div
            @click="handleUpdate(scope.row)"
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
          >{{ scope.row.name }}</div>
        </template>
      </el-table-column>

      <el-table-column
        label="专家照片"
        width="120"
        align="center"
        prop="expertPic"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div v-if="scope.row.picType == '1' || scope.row.picType == '3'">
            <el-image
              class="tablePic"
              :src="scope.row.expertPic[0]"
              alt
              :preview-src-list="scope.row.expertPic"
            />
          </div>
          <div v-if="scope.row.picType == '0' || scope.row.picType == null">
            <div v-if="scope.row.coverType == '0' || scope.row.coverType == null">
              <el-image class="tablePic" :src="morentup[0]" :preview-src-list="morentup" alt />
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="研究方向" align="center" prop="researchDirection" />
      <el-table-column label="院校" align="center" prop="college" />
      <el-table-column label="学历" align="center" prop="education" />
      <el-table-column label="籍贯" align="center" prop="nativePlace" />
      <el-table-column label="政治面貌" align="center" prop="politicsStatus" />
      <el-table-column label="证件类型" align="center" prop="certificateType" />
      <el-table-column label="证件号码" align="center" prop="certificateNumber" />

      <el-table-column
        label="推荐"
        width="120"
        align="center"
        prop="recommend"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          {{
          scope.row.recommend === "0" ? "否" : "是"
          }}
        </template>
      </el-table-column>
      <el-table-column label="置顶" align="center" prop="isTop" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          {{
          scope.row.isTop === "0" ? "否" : "是"
          }}
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        fixed="right"
        width="300"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            v-hasPermi="['thinkTank:expert:approval']"
            @click="handletongguo(scope.row, '')"
          >审核通过</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleexamine(scope.row)"
            v-hasPermi="['thinkTank:expert:approval']"
          >审核不通过</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            v-hasPermi="['thinkTank:expert:query']"
            @click="handleUpdate(scope.row)"
          >预览</el-button>
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

    <!-- 查看新闻动态对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="900px"
      append-to-body
      class="dialog"
    >
      <div style="height: 70vh; overflow: auto; padding: 30px">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" placeholder="请输入姓名" :disabled="reading" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="政治面貌" prop="politicsStatus">
                <el-select v-model="form.politicsStatus" placeholder="请选择政治面貌" :disabled="reading">
                  <el-option
                    v-for="item in zhengzhieOptions"
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
              <el-form-item label="研究方向" prop="researchDirection">
                <el-input v-model="form.researchDirection" placeholder="请输入内容" :disabled="reading" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="院校" prop="college">
                <el-input v-model="form.college" placeholder="请输入院校" :disabled="reading" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="学历" prop="education">
                <el-select v-model="form.education" placeholder="请选择学历" :disabled="reading">
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
              <el-form-item label="籍贯" prop="nativePlace">
                <el-input v-model="form.nativePlace" placeholder="请输入籍贯" :disabled="reading" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="证件类型" prop="certificateType">
                <el-select v-model="form.certificateType" placeholder="请选择证件类型" :disabled="reading">
                  <el-option
                    v-for="item in jiguanOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="证件号码" prop="certificateNumber">
                <el-input
                  v-model="form.certificateNumber"
                  placeholder="请输入证件号码"
                  :disabled="reading"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="专家照片">
                <div v-if="form.picType == '1'">
                  <img class="tablePic" :src="cover4" alt />
                </div>
                <div v-if="form.picType == '3'">
                  <img class="tablePic" :src="cover1" alt />
                  <img class="tablePic" :src="cover2" alt />
                  <img class="tablePic" :src="cover3" alt />
                </div>
                <div v-if="form.picType == '0' || form.picType == null">
                  <img class="tablePic" src="@/assets/images/profile.jpg" alt />
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="简介" prop="introduction">
            <el-input
              v-model="form.introduction"
              type="textarea"
              placeholder="请输入内容"
              :disabled="reading"
            />
          </el-form-item>
          <el-form-item label="内容">
            <div class="ql-editor" v-html="form.content"></div>
          </el-form-item>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="置顶" prop="isTop">
                <el-switch
                  :disabled="reading"
                  v-model="form.isTop"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  active-value="1"
                  inactive-value="0"
                ></el-switch>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="推荐" prop="recommend">
                <el-switch
                  v-model="form.recommend"
                  :disabled="reading"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  active-value="1"
                  inactive-value="0"
                ></el-switch>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="备注" prop="remark">
            <el-input
              :disabled="reading"
              v-model="form.remark"
              type="textarea"
              placeholder="请输入内容"
              maxlength="500" show-word-limit
            />
          </el-form-item>
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
              <div v-if="detelboX" class="bosx" @click="detelbox(index)">X</div>
              <el-form-item label>
                <img class="tablePicee" :src="urlHead + item.url" alt />
              </el-form-item>
              <el-form-item label="发证单位:" style="margin-left: 30px">
                <span>{{ item.issuedBy }}</span>
              </el-form-item>
              <el-form-item label="证书编号:" style="margin-left: 30px">
                <span>{{ item.number }}</span>
              </el-form-item>
              <el-form-item style="margin-left: 30px">
                <label slot="label">有&nbsp;效&nbsp;期</label>
                <span>{{ item.validityTime }}</span>
              </el-form-item>
            </div>
          </el-row>
        </el-form>
      </div>
    </el-dialog>
    <!--审核弹框-->
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
  listpolicyReview,
  updetapolicyReview,
  updetapolicyReviews,
  getexpert,
} from '@/api/xy-think-tank/expertreview'
import { zzzExpert } from '@/api/xy-think-tank/expert'
import TreeSelecter from '../../../components/treeSelect/index.vue'
import upLoadPic from '../../../components/upLoadPic'
export default {
  name: 'News',
  components: { TreeSelecter, upLoadPic },
  data() {
    return {
      morentup: [require('@/assets/images/profile.jpg')],
      opensee: false,
      opentwo: false,
      reading: true,
      cover1: '',
      cover2: '',
      cover3: '',
      cover4: '',
      natureOptions: [],
      zhengzhieOptions: [],
      jiguanOptions: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 新闻动态表格数据
      newsList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      id: '',
      showThreeInput: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        newsTypeId: null,
        title: null,
        content: null,
        author: null,
        source: null,
        type: null,
        classify: null,
        cover: null,
        introduction: null,
        recommend: null,
        views: null,
        collects: null,
        favorites: null,
        isTop: null,
        status: null,
        approveBy: null,
        approveTime: null,
        userId: null,
        deptId: null,
        radio: '',
        bohui: '',
      },
      yesNoOptions: [
        {
          value: '0',
          label: '否',
        },
        {
          value: '1',
          label: '是',
        },
      ],
      treeSelectOptions: [],
      // 表单参数
      form: {},
      // 表单校验
      treeSelectProps: {
        value: 'id', // ID字段名
        label: 'typeName', // 显示名称
        // children: 'children'    // 子级字段名
      },
      rules: {},
    }
  },
  created() {
    this.getList()
    this.education()
    this.politics()
    this.certificate()
  },
  methods: {
    /** 查询新闻动态列表 */
    getList() {
      this.loading = true
      listpolicyReview(this.queryParams).then((response) => {
        this.newsList = response.rows
        this.newsList.forEach((element) => {
          if (element.expertPic !== null) {
            element.expertPic = element.expertPic.split(',')
          }
        })
        this.total = response.total
        this.loading = false
      })
    },
    education() {
      const id = 'xy_expert_education'
      zzzExpert(id).then((response) => {
        this.natureOptions = response.data
      })
    },
    politics() {
      const id = 'xy_expert_politics'
      zzzExpert(id).then((response) => {
        this.zhengzhieOptions = response.data
      })
    },
    certificate() {
      const id = 'xy_expert_certificate'
      zzzExpert(id).then((response) => {
        this.jiguanOptions = response.data
      })
    },
    // 取消按钮
    canceltwo() {
      this.opentwo = false
      this.reset()
    },
    handleFileSuccess(file) {
      this.form.cover = file.src
    },
    // 图片删除
    handleRemove() {
      this.form.cover = ''
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        newsTypeId: null,
        title: null,
        content: null,
        author: null,
        source: null,
        type: null,
        classify: null,
        cover: '',
        introduction: null,
        recommend: '0',
        views: null,
        collects: null,
        favorites: null,
        isTop: '0',
        status: '0',
        remark: null,
        approveBy: null,
        approveTime: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        userId: null,
        deptId: null,
      }
      this.resetForm('form')
    },
    cancel() {
      this.open = false
      this.reset()
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
    // 批量通过/驳回
    resetparam(val) {
      const add = {
        ids: this.ids,
        status: val,
      }
      this.$modal
        .confirm('是否确认操作')
        .then(function () {
          return updetapolicyReviews(add)
        })
        .then(() => {
          this.$modal.msgSuccess('审核成功')
          this.getList()
        })
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
      console.log(this.ids)
      if (selection.length !== '0') {
        this.single = false
      }
      this.multiple = !selection.length
    },
    /** 驳回按钮操作 */
    handleexamine(row) {
      console.log(row)
      this.reset()
      const id = row.id || this.ids
      this.id = row.id
      this.opentwo = true
    },
    /** 预览按钮操作 */
    handleUpdate(row) {
      console.log(row)
      this.reset()
      const id = row.id || this.ids
      getexpert(id).then((response) => {
        this.form = response.data
        let aovss = response.data.expertPic
        if (aovss !== null) {
          if (this.form.picType == '3') {
            this.cover1 = aovss.split(',')[0]
            this.cover2 = aovss.split(',')[1]
            this.cover3 = aovss.split(',')[2]
          }
          if (this.form.picType == '1') {
            this.cover4 = aovss
          }
        }
        if (this.form.content !== null) {
          this.form.content = this.form.content.replace(
            /<img/g,
            "<img style='max-width:90%;height:auto;'"
          )
        }
        this.open = true
        this.showThreeInput = true
        this.title = '专家审核'
      })
    },
    submitsee() {
      this.opensee = false
    },
    // 审核提交
    submit() {
      const add = {
        status: '1',
        remark: this.queryParams.bohui,
        id: this.id,
      }
      this.$modal
        .confirm('是否确认操作')
        .then(function () {
          return updetapolicyReview(add)
        })
        .then(() => {
          this.$modal.msgSuccess('审核成功')
          this.opentwo = false
          this.queryParams.radio = ''
          this.queryParams.bohui = ''
          this.getList()
        })
    },

    /** 通过按钮操作 */
    handletongguo(row) {
      const add = {
        status: '2',
        remark: this.queryParams.bohui,
        id: row.id,
      }
      this.$modal
        .confirm('是否确认通过')
        .then(function () {
          return updetapolicyReview(add)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('审核成功')
        })
        .catch(() => {})
    },
  },
}
</script>
<style lang="scss" scoped>
.tablePic {
  height: 40px;
  width: 70px;
}
.tablePicee {
  width: 144px;
  height: 96px;
  margin-left: -25px;
  margin-top: 10px;
}
.dialog ::v-deep .el-dialog__body {
  padding: 0;
}
</style>
