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
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['thinkTank:expert:add']"
        >新增</el-button>
      </el-col>

      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['thinkTank:expert:remove']"
        >删除</el-button>
      </el-col>-->

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="expertList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column
        width="120"
        label="姓名"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handleUpdate(scope.row, 'red')"
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
            <el-image class="tablePic" :src="morentup[0]" :preview-src-list="morentup" alt />
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="研究方向"
        align="center"
        prop="researchDirection"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="院校" align="center" prop="college" :show-overflow-tooltip="true" />
      <el-table-column label="学历" align="center" prop="education" :show-overflow-tooltip="true" />
      <el-table-column label="籍贯" align="center" prop="nativePlace" :show-overflow-tooltip="true" />
      <el-table-column
        label="政治面貌"
        align="center"
        prop="politicsStatus"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="证件类型"
        align="center"
        prop="certificateType"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="证件号码" align="center" prop="certificateNumber" />
      <el-table-column label="浏览量" align="center" prop="views" />
      <el-table-column label="收藏量" align="center" prop="collects" />
      <el-table-column label="点赞量" align="center" prop="favorites" />
      <el-table-column
        width="120"
        label="推荐"
        align="center"
        prop="recommend"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.recommend"
            @change="switchChange1(scope.row)"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column width="120" label="置顶" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isTop"
            @change="switchChange2(scope.row)"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="statusValue" />
      <el-table-column
        label="操作"
        align="center"
        fixed="right"
        width="300"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row, 'upd')"
            v-hasPermi="['thinkTank:expert:edit']"
            v-if="
              scope.row.status == '0' ||
              scope.row.status == '1' ||
              scope.row.status == '5'
            "
          >修改</el-button>
          <el-button
            type="text"
            size="mini"
            v-if="
              scope.row.status == '0' ||
              scope.row.status == '1' ||
              scope.row.status == '5'
            "
            icon="el-icon-s-check"
            @click="submitFaob(scope.row)"
            v-hasPermi="['thinkTank:expert:submit']"
          >提交审核</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleReject(scope.row)"
            v-if="scope.row.status == '2' || scope.row.status == '3'"
            v-hasPermi="['thinkTank:expert:withdraw']"
          >撤回</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleUpdate(scope.row, 'red')"
            v-hasPermi="['thinkTank:expert:query']"
          >预览</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-if="
              scope.row.status == '0' ||
              scope.row.status == '1' ||
              scope.row.status == '5'
            "
            v-hasPermi="['thinkTank:expert:remove']"
          >删除</el-button>
          <el-button
            v-if="scope.row.status == '3'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handlefabu(scope.row)"
            v-hasPermi="['thinkTank:expert:release']"
          >发布</el-button>
          <el-button
            v-if="scope.row.status == '4'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUnpublish(scope.row)"
            v-hasPermi="['thinkTank:expert:cancel']"
          >取消发布</el-button>
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

    <!-- 添加或修改专家库对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="900px"
      append-to-body
      class="dialogContainer"
    >
      <div
        style="padding: 30px"
        :style="[
          { height: reading ? '70vh' : 'auto' },
          { overflow: reading ? 'auto' : '' },
        ]"
      >
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" placeholder="请输入姓名" :disabled="reading" maxlength="50"
                show-word-limit/>
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
                <el-input v-model="form.researchDirection" placeholder="请输入内容" :disabled="reading" maxlength="100"
                show-word-limit/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="院校" prop="college">
                <el-input v-model="form.college" placeholder="请输入院校" :disabled="reading" maxlength="50"
                show-word-limit/>
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
                <el-input v-model="form.nativePlace" placeholder="请输入籍贯" :disabled="reading" maxlength="50"
                show-word-limit/>
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
                  maxlength="50"
                show-word-limit
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="专家照片" prop="picType">
                <el-radio-group v-if="!reading" v-model="form.picType" @change="changeType">
                  <el-radio label="1">单图</el-radio>
                  <el-radio label="3">三图</el-radio>
                  <el-radio label="0">无封面</el-radio>
                </el-radio-group>
                <div v-if="reading">
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
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20" v-if="!reading">
            <el-col :span="12">
              <el-form-item v-if="form.picType == '1'" prop="expertPic">
                <upLoadPic
                  :cover="cover4"
                  @handleFileSuccess="handleFileSuccess"
                  @handleRemove="handleRemove"
                ></upLoadPic>
              </el-form-item>
              <el-form-item v-if="form.picType == '3'" prop="expertPic" style="width: 900px">
                <upLoadPic
                  :cover="cover1"
                  @handleFileSuccess="handleFileSuccess1"
                  @handleRemove="handleRemove1"
                ></upLoadPic>
                <upLoadPic
                  style="margin-left: 150px; margin-top: -80px"
                  :cover="cover2"
                  @handleFileSuccess="handleFileSuccess2"
                  @handleRemove="handleRemove2"
                ></upLoadPic>
                <upLoadPic
                  style="margin-left: 300px; margin-top: -80px"
                  :cover="cover3"
                  @handleFileSuccess="handleFileSuccess3"
                  @handleRemove="handleRemove3"
                ></upLoadPic>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="简介" prop="introduction">
            <el-input
              v-model="form.introduction"
              type="textarea"
              placeholder="请输入内容"
              :disabled="reading"
              maxlength="500"
                show-word-limit
            />
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <div class="ql-editor" v-if="reading" v-html="form.content"></div>
            <editor v-if="!reading" v-model="form.content" :min-height="192" />
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
              maxlength="500"
                show-word-limit
            />
          </el-form-item>
          <el-form-item label="资质证书" prop="remark">
            <el-button :disabled="reading" @click="tianjia()">添加证书</el-button>
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
                <img class="tablePicee" :src="item.url" alt />
              </el-form-item>
              <el-form-item label="发证单位:" label-width="120px" style="margin-left: 30px">
                <span>{{ item.issuedBy }}</span>
              </el-form-item>
              <el-form-item label="证书编号:" label-width="120px" style="margin-left: 30px">
                <span>{{ item.number }}</span>
              </el-form-item>
              <el-form-item label-width="120px" style="margin-left: 30px">
                <label slot="label">有&nbsp; 效&nbsp; 期:</label>
                <span>{{ item.validityTime }}</span>
              </el-form-item>
            </div>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer" v-if="yulan">
        <el-button type="primary" :disabled="shenhe" @click="submitForm('add')">提交审核</el-button>
        <el-button type="primary" @click="submitForm('att')">保存草稿</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>

    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="opentwo"
      width="450px"
      append-to-body
    >
      <el-form :span="12" label-width="120px" style="margin-top: 30px">
        <el-form-item label style="margin-left: 30px">
          <upLoadPic
            :cover="boxurl"
            @handleFileSuccess="handleFileSuccess4"
            @handleRemove="handleRemove4"
          ></upLoadPic>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="20">
            <el-form-item label="发证单位" label-width="120px" style="margin-left: 30px">
              <el-input v-model="boxissuedBy" placeholder="请输入发证单位·" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="20">
            <el-form-item label="证书编号" label-width="120px" style="margin-left: 30px">
              <el-input v-model="boxnumber" placeholder="请输入证书编号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" style="margin-left: 30px">
            <el-form-item label-width="120px">
              <label slot="label">有&nbsp;效&nbsp;期</label>
              <el-date-picker
                clearable
                v-model="boxvalidityTime"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择时间"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-right: 120px">
        <el-button type="primary" @click="submitwo" style="margin-right: 30px">确 定</el-button>
        <el-button @click="canceltwo">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 取消发布/撤回弹窗 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="visible"
      width="500px"
      append-to-body
    >
      <el-row :gutter="20">
        <el-form :span="12">
          <el-form-item :label="text">
            <el-input v-model="remark" type="textarea" :placeholder="placeholder" />
          </el-form-item>
        </el-form>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitReject">确 定</el-button>
        <el-button @click="cancelReject">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listExpert,
  getExpert,
  delExpert,
  addExpert,
  updateExpert,
  afabuExpert,
  zzzExpert,
  submit,
  withdraw,
  updateRecTop,
} from '@/api/xy-think-tank/expert'
import { updetapolicyReview } from '@/api/xy-think-tank/expertreview.js'

import TreeSelecter from '../../../components/treeSelect/index.vue'
import upLoadPic from '../../../components/upLoadPic'
export default {
  name: 'Expert',
  components: { TreeSelecter, upLoadPic },
  data() {
    return {
      morentup: [require('@/assets/images/profile.jpg')],
      aastatused: '',
      youxiao: '',
      idss: '',
      opentwo: false,
      reading: false,
      shenhe: false,
      yulan: true,
      fengmian1: '',
      fengmian2: '',
      fengmian3: '',
      opensee: false,
      boxurl: '',
      boxnumber: '',
      boxvalidityTime: '',
      boxissuedBy: '',
      detelboX: true,
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
      valueid: 1,
      cover1: '',
      cover2: '',
      cover3: '',
      cover4: '',
      cover5: '',
      natureOptions: [],
      zhengzhieOptions: [],
      jiguanOptions: [],
      treeSelectOptions: [],
      showThreeInput: true,
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
      // 专家库表格数据
      expertList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        deptId: null,
        name: null,
        introduction: null,
        content: null,
        college: null,
        education: null,
        nativePlace: null,
        politicsStatus: null,
        certificateType: null,
        certificateNumber: null,
        organization: null,
        credentialNumber: null,
        credentialValidity: null,
        researchDirection: null,
        mainAchievement: null,
        expertPic: null,
        picType: null,
        certificatePic: null,
        status: null,
        recommend: null,
        isTop: null,
        views: null,
        collects: null,
        favorites: null,
        approveBy: null,
        approveTime: null,
      },
      treeSelectProps: {
        value: 'id', // ID字段名
        label: 'typeName', // 显示名称
        // children: 'children'    // 子级字段名
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [{ required: true, message: '名字不能为空', trigger: 'blur' }],
        content: [{ required: true, message: '内容不能为空', trigger: 'blur' }],
        education: [
          { required: true, message: '学历不能为空', trigger: 'change' },
        ],
        politicsStatus: [
          { required: true, message: '政治面貌不能为空', trigger: 'change' },
        ],
        certificateType: [
          { required: true, message: '证件类型不能为空', trigger: 'change' },
        ],
        picType: [
          {
            required: true,
            message: '专家图片类型不能为空',
            trigger: 'change',
          },
        ],
      },
      currentId: '', // 操作行id
      remark: '', // 撤回，取消发布备注
      visible: false, // 撤回，取消发布弹窗
      text: '', // 弹窗小标题
      placeholder: '', //文本框默认文字
      rejectType: null, // 撤回or取消发布 0:取消发布 , 1:撤回
    }
  },
  watch: {
    'form.content'(val) {
      let reg = /^(\<p\>)\s+(\<\/p\>)$/g
      if (val == '<p><br></p>' || reg.test(val)) {
        this.form.content = ''
      }
    },
  },
  created() {
    this.getList()
    this.education()
    this.politics()
    this.certificate()
  },
  methods: {
    /** 查询专家库列表 */
    getList() {
      this.loading = true
      listExpert(this.queryParams).then((response) => {
        this.expertList = response.rows
        this.expertList.forEach((element) => {
          if (element.expertPic !== null) {
            element.expertPic = element.expertPic.split(',')
          }
        })
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    canceltwo() {
      this.opentwo = false
      this.boxissuedBy = ''
      this.boxurl = ''
      this.boxvalidityTime = ''
      this.boxnumber = ''
    },
    // 添加证书
    tianjia() {
      this.boxurl = ''
      this.boxnumber = ''
      this.boxvalidityTime = ''
      this.boxissuedBy = ''
      this.opentwo = true
      this.title = '添加资质证书'
    },
    detelbox(row) {
      this.form.credentials.splice(row, 1)
    },
    switchChange1(n) {
      const add = {
        id: n.id,
        recommend: n.recommend,
        status: 0,
      }
      updateRecTop(add).then((response) => {
        this.$modal.msgSuccess('修改成功')
        this.getList()
      })
    },
    switchChange2(n) {
      const add = {
        id: n.id,
        isTop: n.isTop,
        status: 1,
      }
      updateRecTop(add).then((response) => {
        this.$modal.msgSuccess('修改成功')
        this.getList()
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
    handleFileSuccess(file) {
      this.cover4 = file.src
    },
    handleFileSuccess1(file) {
      this.cover1 = file.src
    },
    handleFileSuccess2(file) {
      this.cover2 = file.src
    },
    handleFileSuccess3(file) {
      this.cover3 = file.src
    },
    handleFileSuccess4(file) {
      this.boxurl = file.src
    },
    // 图片删除
    handleRemove() {
      this.cover4 = ''
    },
    handleRemove1() {
      this.cover1 = ''
    },
    handleRemove2() {
      this.cover2 = ''
    },
    handleRemove3() {
      this.cover3 = ''
    },
    handleRemove4() {
      this.cover5 = ''
    },
    // 发布
    handlefabu(row) {
      this.currentId = row.id
      this.aastatused = '2'
      this.submit()
    },
    submit() {
      const add = {
        id: this.currentId,
        status: this.aastatused,
      }
      afabuExpert(add).then((response) => {
        if (add.status == '2') {
          this.$modal.msgSuccess('发布成功')
        } else {
          this.$modal.msgSuccess('退回成功')
        }
        this.getList()
      })
    },
    // 取消发布按钮
    handleUnpublish(row) {
      this.currentId = row.id
      this.aastatused = '3'
      this.rejectType = 0
      this.visible = true
      this.title = '取消发布'
      this.text = '取消原因'
      this.placeholder = '请输入取消发布原因'
    },
    // 撤回按钮
    handleReject(row) {
      this.currentId = row.id
      this.rejectType = 1
      this.visible = true
      this.title = '撤回'
      this.text = '撤回原因'
      this.placeholder = '请输入撤回原因'
    },
    // 取消按钮
    cancelReject() {
      this.visible = false
      this.remark = ''
      this.title = ''
      this.text = ''
      this.placeholder = ''
      this.rejectType = null
      this.currentId = ''
    },
    // 取消发布及撤回确认
    submitReject() {
      switch (this.rejectType) {
        case 0:
          this.submit()
          break
        case 1:
          withdraw({
            id: this.currentId,
            remark: this.remark,
          }).then(() => {
            this.$modal.msgSuccess('撤回成功')
            this.getList()
          })
          break
      }
      this.cancelReject()
    },
    changeType(val) {
      if (val == 1) {
        this.cover4 = this.cover1
      } else if (val == 3) {
        this.cover1 = this.cover4
      } else {
        this.cover1 = ''
        this.cover2 = ''
        this.cover3 = ''
        this.cover4 = ''
      }
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    submitwo() {
      this.valueid = this.valueid + 1
      const add = {
        url: this.boxurl,
        number: this.boxnumber,
        validityTime: this.boxvalidityTime,
        issuedBy: this.boxissuedBy,
      }
      this.form.credentials.push(add)
      this.opentwo = false
    },
    // 表单重置
    reset() {
      this.cover1 = ''
      this.cover2 = ''
      this.cover3 = ''
      this.cover4 = ''
      this.reading = false
      this.form = {
        id: null,
        userId: null,
        deptId: null,
        name: null,
        introduction: null,
        content: null,
        college: null,
        education: null,
        nativePlace: null,
        politicsStatus: '0',
        certificateType: null,
        certificateNumber: null,
        organization: null,
        credentialNumber: null,
        credentialValidity: null,
        researchDirection: null,
        mainAchievement: null,
        expertPic: null,
        picType: null,
        certificatePic: null,
        status: '0',
        remark: null,
        recommend: null,
        isTop: null,
        views: null,
        collects: null,
        favorites: null,
        delFlag: null,
        approveBy: null,
        approveTime: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        credentials: [],
      }
      this.resetForm('form')
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.yulan = true
      this.detelboX = true
      this.title = '添加专家库'
    },
    /** 修改按钮操作 */
    handleUpdate(row, val) {
      this.reset()
      if (row.status == '0' || row.status == '1' || row.status == '5') {
        this.shenhe = false
      } else {
        this.shenhe = true
      }
      this.cover4 = ''
      this.cover1 = ''
      this.cover2 = ''
      this.cover3 = ''
      const id = row.id || this.ids

      getExpert(id).then((response) => {
        this.form = response.data
        if (this.form.content !== null) {
          this.form.content = this.form.content.replace(
            /<img/g,
            "<img style='max-width:90%;height:auto;'"
          )
        }
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
        if (val == 'red') {
          this.reading = true
          this.detelboX = false
          this.title = '预览专家库'
          this.yulan = false
        } else {
          this.reading = false
          this.title = '修改专家库'
          this.detelboX = true
          this.yulan = true
        }
        this.open = true
      })
    },
    // 列表提交审核
    submitFaob(row) {
      const add = {
        id: row.id,
      }
      this.$modal
        .confirm('是否确认操作？')
        .then(function () {
          return submit(add)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('提交成功')
          this.relbtn = false
        })
    },
    /** 提交按钮 */
    submitForm(val) {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.picType == '3') {
            if (!this.cover1 || !this.cover2 || !this.cover3) {
              return this.$modal.msgError('请上传三张照片')
            }
            this.form.expertPic =
              this.cover1 + ',' + this.cover2 + ',' + this.cover3
          } else if (this.form.picType == '1') {
            if (!this.cover4) {
              return this.$modal.msgError('请上传一张照片')
            }
            this.form.expertPic = this.cover4
          } else {
            this.form.expertPic = ''
          }
          if (this.form.id != null) {
            if (val == 'add') {
              this.form.addStatus = '0'
            }
            if (val == 'att') {
              this.form.addStatus = ''
            }
            const att = this.form
            this.$modal
              .confirm('是否确认？')
              .then(function () {
                return updateExpert(att)
              })
              .then(() => {
                this.$modal.msgSuccess('修改成功')
                this.open = false
                this.getList()
              })
          } else {
            if (val == 'add') {
              this.form.addStatus = '2'
            }
            if (val == 'att') {
              this.form.addStatus = '0'
            }
            const add = this.form
            this.$modal
              .confirm('是否确认？')
              .then(function () {
                return addExpert(add)
              })
              .then(() => {
                this.$modal.msgSuccess('新增成功')
                this.open = false
                this.getList()
              })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认删除专家库编号为"' + ids + '"的数据项？')
        .then(function () {
          return delExpert(ids)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('删除成功')
        })
        .catch(() => {})
    },
  },
}
</script>
<style scoped>
.bosx {
  margin-left: 230px;
  margin-top: 0px;
  font-size: 16px;
  color: red;
}
.tablePic {
  height: 48px;
  width: 72px;
}
.tablePicee {
  width: 144px;
  height: 96px;
  margin-left: -25px;
  margin-top: 10px;
}
.dialogContainer ::v-deep .el-dialog__body {
  padding: 0;
}
</style>
