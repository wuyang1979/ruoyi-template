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
      <el-form-item label="应用名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入应用名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属企业" prop="enterpriseBelong">
        <el-input
          v-model="queryParams.enterpriseBelong"
          placeholder="请输入所属企业"
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
          v-hasPermi="['application:management:add']"
        >新增</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['application:management:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['application:management:remove']"
        >删除</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="managementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="应用名称"
        align="left"
        prop="name"
        width="300"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handlesee(scope.row)"
          >{{ scope.row.name }}</div>
        </template>
      </el-table-column>
      <el-table-column
        label="Handle码"
        width="300"
        align="left"
        prop="handleCode"
        style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
      >
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handleUrl(scope.row)"
          >
            <span>{{ scope.row.handleCode }}</span>
            <el-image
              style="width: 10px; height: 10px;visibility: hidden;"
              :src="scope.row.handleQrUrl"
              :ref="'listPreview'+scope.row.handleCode"
              fit="contain"
              :preview-src-list="[scope.row.handleQrUrl]"
            ></el-image>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="封面图" align="center" prop="cover" width="120">
        <template slot-scope="scope">
          <div v-if="scope.row.cover == null || scope.row.cover == ''">
            <el-image class="tablePic" :src="morentup[0]" :preview-src-list="morentup" alt />
          </div>
          <div v-else>
            <el-image
              class="tablePic"
              :src="scope.row.cover?scope.row.cover.split(',')[0]:''"
              alt
              :preview-src-list="scope.row.cover?scope.row.cover.split(','):[]"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="分类" align="center" prop="typeName" width="120" />
      <el-table-column
        label="所属企业"
        align="center"
        prop="enterpriseBelong"
        width="180"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="扫码下载" align="center" prop="downloadQrUrl" width="200">
        <template slot-scope="scope">
          <div>
            <el-image
              v-if="scope.row.downloadQrUrl"
              class="tableQr"
              :src="scope.row.downloadQrUrl"
              alt
              :preview-src-list="[scope.row.downloadQrUrl]"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="作者" align="center" prop="auth" width="120" />
      <el-table-column label="下载量" align="center" prop="download" width="120" />
      <el-table-column label="浏览量" align="center" prop="views" width="120" />
      <el-table-column label="收藏量" align="center" prop="collects" width="120" />
      <el-table-column label="点赞量" align="center" prop="favorites" width="120" />
      <el-table-column label="首页推荐" align="center" prop="recommend" width="120">
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
      <el-table-column width="120" label="置顶" align="center">
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
      <el-table-column label="状态" align="center" prop="statusValue" width="120" />
      <el-table-column
        label="操作"
        align="center"
        width="300"
        fixed="right"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-if="scope.row.status == '0' || scope.row.status == '1' || scope.row.status == '5'"
            v-hasPermi="['application:management:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-if="scope.row.status == '0' || scope.row.status == '1' || scope.row.status == '5'"
            v-hasPermi="['application:management:remove']"
          >删除</el-button>
          <el-button
            v-if="scope.row.status == '3'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleRelease(scope.row)"
            v-hasPermi="['application:management:release']"
          >发布</el-button>
          <el-button
            v-if="scope.row.status == '4'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUnpublish(scope.row)"
            v-hasPermi="['application:management:cancel']"
          >取消发布</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleSubmit(scope.row)"
            v-if="scope.row.status == '0' || scope.row.status == '1' || scope.row.status == '5'"
            v-hasPermi="['application:management:submit']"
          >提交审核</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleReject(scope.row)"
            v-if="scope.row.status == '2' || scope.row.status == '3'"
            v-hasPermi="['application:management:withdraw']"
          >撤回</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlesee(scope.row)"
            v-hasPermi="['application:management:query']"
          >预览</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改应用信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      v-if="open"
      width="1000px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="应用名称" prop="name">
          <el-input v-model.trim="form.name" placeholder="请输入应用名称" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="所属企业" prop="enterpriseBelong">
          <el-input
            v-model="form.enterpriseBelong"
            placeholder="请输入所属企业"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input
                v-model="form.contactPerson"
                placeholder="请输入联系人"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系方式" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系方式" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类" prop="typeId">
              <TreeSelecter
                :props="treeSelectProps"
                v-model="form.typeId"
                :options="treeSelectOptions"
                :value="form.typeId"
              ></TreeSelecter>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="作者" prop="auth">
              <el-input v-model="form.auth" placeholder="请输入作者" maxlength="50" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="下载地址" prop="downloadUrl">
              <el-input v-model="form.downloadUrl" placeholder="请输入下载地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item>
              <el-button
                type="primary"
                :disabled="downloadUrlDisabled"
                @click="downloadQR(form.downloadUrl)"
              >生成二维码</el-button>
            </el-form-item>
            <el-form-item v-if="form.downloadQrUrl" label-width="100px">
              <el-image style="width:150px;height:150px;" :src="form.downloadQrUrl" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Handle码" prop="handleCode">
              <el-input v-model="form.handleCode" placeholder="请输入Handle码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item>
              <el-button
                type="primary"
                :disabled="handleCodeDisabled"
                @click="handleCodeQR(form.handleCode)"
              >生成二维码</el-button>
            </el-form-item>
            <el-form-item v-if="form.handleQrUrl" label-width="100px">
              <el-image style="width:150px;height:150px;" :src="form.handleQrUrl" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="应用封面图" prop="cover">
          <upload-imgs :images="form.cover" @upload="uploadSuccess"></upload-imgs>
        </el-form-item>
        <!-- <el-form-item label="应用视频" prop="videoUrl">
          <upload-video :videos="videos" @change="getVideo"></upload-video>
        </el-form-item>-->
        <el-form-item label="摘要" prop="introduce">
          <el-input
            v-model="form.introduce"
            type="textarea"
            rows="4"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <editor v-model="form.content" :min-height="192" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="首页推荐" prop="recommend">
              <el-switch
                v-model="form.recommend"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="1"
                inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="置顶" prop="isTop">
              <el-switch
                v-model="form.isTop"
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
            v-model="form.remark"
            type="textarea"
            rows="4"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm('add')" :disabled="shenhe">提交审核</el-button>
        <el-button type="primary" @click="submitForm('att')">保存草稿</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="opensee"
      v-if="opensee"
      class="view-dialog"
      width="1000px"
      append-to-body
    >
      <div style="height: 70vh;overflow: auto;">
        <div class="view-box">
          <div class="view-head">
            <div class="head-left">
              <el-carousel
                ref="carousel"
                v-if="form.cover == null || form.cover == ''"
                height="150px"
                style="background: #f7f7f7"
              >
                <el-carousel-item v-for="item in morentup" :key="item">
                  <el-image :src="item" fit="contain" style="width: 100%; height: 100%" />
                </el-carousel-item>
              </el-carousel>
              <el-carousel ref="carousel" v-else height="150px" style="background: #f7f7f7">
                <el-carousel-item v-for="item in form.cover" :key="item">
                  <el-image :src="item" fit="contain" style="width: 100%; height: 100%" />
                </el-carousel-item>
              </el-carousel>
            </div>
            <div class="head-right">
              <div class="head-right-top">
                <div class="head-right-top-left">
                  <div>{{form.name}}</div>
                  <div>分类：{{form.typeName}}</div>
                  <div>公司名称：{{form.enterpriseBelong}}</div>
                </div>
                <div class="head-right-top-right">
                  <div class="self-button" @click="download(1)">
                    <span>点击下载应用</span>
                  </div>
                  <div class="self-button download-btn" @click="download(2)">
                    <span>扫 码 下 载</span>
                    <el-image
                      v-if="form.downloadQrUrl"
                      style="width: 26px; height: 26px;margin-left: 10px"
                      :src="form.downloadQrUrl"
                      ref="preview"
                      fit="contain"
                      :preview-src-list="[form.downloadQrUrl]"
                    ></el-image>
                  </div>
                </div>
              </div>
              <div class="head-right-bot">Handle码：{{form.handleCode}}</div>
            </div>
          </div>
          <div class="view-introduce">
            <div class="introduce-title">应用介绍</div>
            <div v-html="form.content"></div>
          </div>
        </div>
      </div>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="visible"
      width="500px"
      append-to-body
    >
      <el-row :gutter="20">
        <el-form :span="12">
          <el-form-item :label="reason">
            <el-input v-model="remark" type="textarea" :placeholder="'请输入'+reason" />
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
  listManagement,
  getManagement,
  delManagement,
  addManagement,
  updateManagement,
  fabuManagement,
  fenleiInfo,
  generateQr,
  submit,
  withdraw,
  recommendTop,
} from '@/api/application/management'
import TreeSelecter from '@/components/treeSelect/index.vue'
import { validURL } from '@/utils/validate.js'
import { validPhone, validHandleCode } from '@/utils/wdcspValidate.js'
import UploadImgs from '@/components/UploadImgs'
import UploadVideo from '@/components/uploadVideo'

export default {
  name: 'ApplicationManagement',
  components: { TreeSelecter, UploadImgs, UploadVideo },
  data() {
    var checkDownloadUrl = (rule, value, callback) => {
      if (!value) {
        // return callback(new Error('下载地址不能为空'))
        return callback()
      }
      setTimeout(() => {
        if (!validURL(value)) {
          callback(new Error('请输入网址'))
        } else {
          callback()
        }
      }, 300)
    }
    var checkContactPhone = (rule, value, callback) => {
      if (!value) {
        return callback()
      }
      setTimeout(() => {
        if (!validPhone(value)) {
          callback(new Error('请输入电话号或手机号'))
        } else {
          callback()
        }
      }, 300)
    }
    var checkHandleCode = (rule, value, callback) => {
      if (!value) {
        return callback()
      }
      setTimeout(() => {
        if (!validHandleCode(value)) {
          callback(new Error('请以 86 开头'))
        } else {
          callback()
        }
      }, 300)
    }
    return {
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
      // 应用信息表格数据
      managementList: [],
      // 预览判断条件
      opensee: false,
      // 弹窗提交审核按钮是否可用
      shenhe: true,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        enterpriseBelong: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: '应用名称不能为空', trigger: 'blur' },
        ],
        typeId: [
          { required: true, message: '分类不能为空', trigger: 'change' },
        ],
        downloadUrl: [
          { validator: checkDownloadUrl, trigger: 'blur' },
          // { required: true, message: '下载地址不能为空', trigger: 'blur' },
        ],
        contactPhone: [{ validator: checkContactPhone, trigger: 'blur' }],
        handleCode: [{ validator: checkHandleCode, trigger: 'blur' }],
        content: [
          { required: true, message: '产品内容不能为空', trigger: 'blur' },
        ],
        status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
        delFlag: [
          { required: true, message: '删除标志不能为空', trigger: 'blur' },
        ],
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' },
        ],
        userId: [
          { required: true, message: '用户id不能为空', trigger: 'blur' },
        ],
        deptId: [
          { required: true, message: '部门id不能为空', trigger: 'blur' },
        ],
      },
      treeSelectProps: {
        value: 'id', // ID字段名
        label: 'typeName', // 显示名称
        // children: 'children'    // 子级字段名
      },
      treeSelectOptions: [],
      videos: [],
      morentup: [require('@/assets/images/profile.jpg')],
      currentId: '', // 操作行id
      remark: '', // 撤回，取消发布备注
      visible: false, // 撤回，取消发布弹窗
      rejectType: null, // 撤回or取消发布 0:取消发布 , 1:撤回
      reason: '', // 撤回or取消发布 文字
    }
  },
  computed: {
    downloadUrlDisabled() {
      return !validURL(this.form.downloadUrl)
    },
    handleCodeDisabled() {
      return !validHandleCode(this.form.handleCode)
    },
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
    this.fenlei()
  },
  methods: {
    /** 查询应用信息列表 */
    getList() {
      this.loading = true
      listManagement(this.queryParams).then((response) => {
        this.managementList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 新增产品分类
    fenlei() {
      fenleiInfo(this.queryParams).then((res) => {
        this.treeSelectOptions = res.data
      })
    },
    // 发布按钮
    handleRelease(row) {
      this.currentId = row.id
      this.aastatused = '2'
      const id = row.id || this.ids
      this.submit()
    },
    // 取消发布按钮
    handleUnpublish(row) {
      this.currentId = row.id
      this.aastatused = '3'
      this.rejectType = 0
      this.visible = true
      this.title = '取消发布'
      this.reason = '取消原因'
    },
    // 撤回
    handleReject(row) {
      this.currentId = row.id
      this.rejectType = 1
      this.visible = true
      this.title = '撤回'
      this.reason = '撤回原因'
    },
    // 预览
    handlesee(row) {
      this.form = { ...row }
      this.form.cover = this.form.cover ? this.form.cover.split(',') : []
      this.opensee = true
      this.title = '预览'
    },
    handleUrl(row) {
      this.$refs['listPreview' + row.handleCode].showViewer = true
    },
    switchChange1(n) {
      const add = {
        status: 0,
        id: n.id,
        recommend: n.recommend,
      }
      recommendTop(add).then((res) => {
        this.$modal.msgSuccess('修改成功')
        this.getList()
      })
    },
    switchChange2(n) {
      const att = {
        status: 1,
        id: n.id,
        isTop: n.isTop,
      }
      recommendTop(att).then((response) => {
        this.$modal.msgSuccess('修改成功')
        this.getList()
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
        id: null,
        name: null,
        enterpriseBelong: null,
        contactPerson: null,
        contactPhone: null,
        typeId: null,
        auth: null,
        downloadUrl: null,
        handleCode: null,
        cover: null,
        videoUrl: null,
        introduce: null,
        content: null,
        recommend: '0',
        isTop: '0',
        download: null,
        views: null,
        collects: null,
        favorites: null,
        remark: null,
        status: '0',
        delFlag: null,
        publishTime: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        userId: null,
        deptId: null,
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
      // 选中的数据是否包含不可删除和编辑的数据
      let unable = selection.find(
        (ele) => ele.status !== '0' && ele.status !== '1' && ele.status !== '5'
      )
      this.single = selection.length !== 1 || !!unable
      this.multiple = !selection.length || !!unable
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.shenhe = false
      this.reset()
      this.open = true
      this.videos = []
      this.title = '添加应用信息'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      if (row.status == '0' || row.status == '1' || row.status == '5') {
        this.shenhe = false
      } else {
        this.shenhe = true
      }
      this.reset()
      const id = row.id || this.ids
      getManagement(id).then((response) => {
        this.form = response.data
        let obj = {},
          arr = []
        if (this.form.videoUrl) {
          this.form.videoUrl.split(',').forEach((ele) => {
            obj.url = ele
            arr.push(obj)
          })
        }
        this.videos = arr
        this.open = true
        this.title = '修改应用信息'
      })
    },
    // 二维码请求
    downloadQR(val) {
      const add = {
        downloadUrl: val,
        type: 2,
      }
      if (val !== null) {
        generateQr(add).then((res) => {
          this.$set(this.form, 'downloadQrUrl', res.msg)
        })
      } else if (val == null) {
        this.$modal.confirm('请填写下载地址')
      }
    },
    handleCodeQR(val) {
      const add = {
        handleCode: val,
        type: 1,
      }
      if (val !== null) {
        generateQr(add).then((res) => {
          this.$set(this.form, 'handleQrUrl', res.msg)
        })
      } else if (val == null) {
        this.$modal.confirm('请填写handle码')
      }
    },
    // 图片上传成功
    uploadSuccess(val) {
      this.form.cover = val.join(',')
    },
    // 视频上传成功
    getVideo(event) {
      if (event && event.length > 0) {
        this.videos = event
        this.form.videoUrl = event.map((ele) => ele.url).join(',')
      } else {
        this.videos = []
        this.form.videoUrl = ''
      }
    },
    // 发布提交
    submit() {
      const add = {
        id: this.currentId,
        status: this.aastatused,
        remark: this.rejectType == 0 ? this.remark : null,
      }
      fabuManagement(add).then((response) => {
        if (add.status == '2') {
          this.$modal.msgSuccess('发布成功')
        } else {
          this.$modal.msgSuccess('取消发布成功')
        }
        this.getList()
      })
    },
    // 撤回提交
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
    cancelReject() {
      this.visible = false
      this.reason = ''
      this.remark = ''
      this.rejectType = null
      this.currentId = ''
    },
    // 提交审核按钮操作
    handleSubmit(row) {
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
        })
    },
    /** 提交按钮 */
    submitForm(val) {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            if (val == 'add') {
              this.form.addStatus = '0'
            } else {
              this.form.addStatus = ''
            }
            const add = this.form
            this.$modal
              .confirm('是否确认操作？')
              .then(function () {
                return updateManagement(add)
              })
              .then(() => {
                this.$modal.msgSuccess('修改成功')
                this.open = false
                this.getList()
              })
          } else {
            if (val == 'add') {
              this.form.addStatus = '2'
            } else {
              this.form.addStatus = '0'
            }
            const att = this.form
            this.$modal
              .confirm('是否确认操作？')
              .then(function () {
                return addManagement(att)
              })
              .then(() => {
                this.$modal.msgSuccess('修改成功')
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
        .confirm('是否确认删除应用信息编号为"' + ids + '"的数据项？')
        .then(function () {
          return delManagement(ids)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('删除成功')
        })
        .catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'application/management/export',
        {
          ...this.queryParams,
        },
        `management_${new Date().getTime()}.xlsx`
      )
    },
    download(index) {
      if (index == 1) {
        window.open(this.form.downloadUrl)
      } else {
        if (this.form.downloadQrUrl) {
          this.$refs.preview.showViewer = true
        }
      }
    },
  },
}
</script>
<style lang="scss" scoped>
.tablePic {
  height: 48px;
  width: 72px;
}
.tableQr {
  height: 48px;
  width: 48px;
}
.view-dialog ::v-deep .el-dialog__body {
  padding: 0;
  .view-box {
    font-size: 16px;
    .view-head {
      padding: 30px;
      display: flex;
      justify-content: space-between;
      .head-left {
        width: 33%;
      }
      .head-right {
        width: 64%;
        .head-right-top {
          display: flex;
          justify-content: space-between;
          .head-right-top-left > div {
            margin-top: 15px;
          }
          .head-right-top-right > div {
            margin-top: 15px;
          }
          .head-right-top-right {
            margin-right: 40px;
            .self-button {
              background: #1890ff;
              height: 40px;
              line-height: 40px;
              border-radius: 4px;
              width: 200px;
              text-align: center;
              cursor: pointer;
              span {
                color: #fff;
                display: inline-block;
                width: 100px;
                font-size: 14px;
              }
            }
            .download-btn {
              display: flex;
              align-items: center;
              span {
                margin-left: 50px;
                letter-spacing: 2.4px;
              }
            }
          }
        }
        .head-right-bot {
          color: #999;
          margin-top: 15px;
        }
      }
    }
    .view-introduce {
      padding: 0 30px 20px;
      .introduce-title {
        font-size: 18px;
        padding-bottom: 20px;
        border-bottom: 1px solid #999;
      }
      img {
        display: block;
        margin: auto;
        max-width: 100%;
      }
    }
  }
}
</style>
