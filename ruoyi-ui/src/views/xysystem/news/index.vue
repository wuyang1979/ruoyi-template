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
      <el-form-item label="新闻标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入新闻标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input
          v-model="queryParams.author"
          placeholder="请输入作者"
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
          v-hasPermi="['xyNews:news:add']"
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
          v-hasPermi="['syNews:news:remove']"
        >删除</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="newsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :show-overflow-tooltip="true" />

      <el-table-column
        label="标题"
        align="left"
        prop="title"
        width="300"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div
            @click="handlersee(scope.row)"
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
          >{{ scope.row.title }}</div>
        </template>
      </el-table-column>

      <el-table-column
        label="封面"
        width="120"
        align="center"
        prop="cover"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div v-if="scope.row.coverType == '1' || scope.row.coverType == '3'">
            <el-image
              class="tablePic"
              :src="scope.row.cover[0]"
              alt
              :preview-src-list="scope.row.cover"
            />
          </div>
          <div v-if="scope.row.coverType == '0' || scope.row.coverType == null">
            <el-image class="tablePic" :src="morentup[0]" :preview-src-list="morentup" alt />
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="分类"
        width="180"
        align="center"
        prop="typeName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="类型"
        width="180"
        align="center"
        prop="type"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="来源"
        width="180"
        align="left"
        prop="source"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="作者"
        width="180"
        align="center"
        prop="author"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="简介"
        width="180"
        align="left"
        prop="introduction"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="浏览量"
        width="120"
        align="center"
        prop="views"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="收藏量"
        width="120"
        align="center"
        prop="collects"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="点赞量"
        width="120"
        align="center"
        prop="favorites"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="推荐"
        width="120"
        align="center"
        prop="recommend"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.recommend"
            @change="switchChange(scope.row, '0')"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        label="置顶"
        width="120"
        align="center"
        prop="isTop"
        :show-overflow-tooltip="true"
      >
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
      <el-table-column
        label="状态"
        width="120"
        align="center"
        prop="statusValue"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column
        width="300"
        fixed="right"
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-if="scope.row.status == '0' || scope.row.status == '1' || scope.row.status == '5'"
            v-hasPermi="['xyNews:news:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-if="scope.row.status == '0' || scope.row.status == '1' || scope.row.status == '5'"
            v-hasPermi="['xyNews:news:remove']"
          >删除</el-button>
          <el-button
            type="text"
            size="mini"
            v-if="scope.row.status == '0' || scope.row.status == '1' || scope.row.status == '5'"
            icon="el-icon-s-check"
            @click="submitFaob(scope.row)"
            v-hasPermi="['xyNews:news:submit']"
          >提交审核</el-button>
          <el-button
            v-if="scope.row.status == '3'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handlerelease(scope.row)"
            v-hasPermi="['xyNews:news:release']"
          >发布</el-button>
          <el-button
            v-if="scope.row.status == '4'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handlertuihui(scope.row)"
            v-hasPermi="['xyNews:news:cancel']"
          >取消发布</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleReject(scope.row)"
            v-if="scope.row.status == '2' || scope.row.status == '3'"
            v-hasPermi="['xyNews:news:withdraw']"
          >撤回</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlersee(scope.row)"
            v-hasPermi="['xyNews:news:query']"
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

    <!-- 添加或修改新闻动态对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="900px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="新闻标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入新闻标题" maxlength="100" show-word-limit />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="新闻分类" prop="newsTypeId">
              <TreeSelecter
                :props="treeSelectProps"
                v-model="form.newsTypeId"
                :options="treeSelectOptions"
                :value="form.newsTypeId"
              ></TreeSelecter>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="新闻类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择新闻">
                <el-option
                  v-for="item in natureOptions"
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
            <el-form-item label="作者" prop="author">
              <el-input v-model="form.author" placeholder="请输入作者" maxlength="50" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="来源" prop="source">
              <el-input v-model="form.source" placeholder="请输入来源" maxlength="100" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-form-item label="封面" prop="coverType">
            <el-radio-group v-model="form.coverType" @change="changeType">
              <el-radio label="1">单图</el-radio>
              <el-radio label="3">三图</el-radio>
              <el-radio label="0">无封面</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item v-if="form.coverType == '1'" prop="cover">
              <upLoadPic
                :cover="cover4"
                @handleFileSuccess="handleFileSuccess"
                @handleRemove="handleRemove"
              ></upLoadPic>
            </el-form-item>
            <el-form-item v-if="form.coverType == '3'" prop="cover" style="width: 900px">
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
            rows="4"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="新闻内容" ref="contentValiRef" prop="content">
              <editor v-model="form.content" :min-height="192" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
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
          <el-col :span="12">
            <el-form-item label="推荐" prop="recommend">
              <el-switch
                v-model="form.recommend"
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

    <!-- 详情弹框 -->

    <el-dialog
      :close-on-click-modal="false"
      :visible.sync="opensee"
      width="900px"
      class="view-dialog"
      append-to-body
      :title="title"
    >
      <div style="height: 70vh;overflow:auto;padding: 30px">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px" class="formll">
          <el-row>
            <div class="biaoti">{{ form.title }}</div>
          </el-row>
          <el-row>
            <el-col style="display: flex; flex-direction: row; margin-left: 15px">
              <span v-if="form.author">{{ form.author }}:</span>
              <span v-if="form.createTime">{{ form.createTime }}:</span>
              <span v-if="form.source">{{ form.source }}</span>
            </el-col>
          </el-row>
          <el-divider content-position="right"></el-divider>
          <el-row>
            <el-col>
              <div class="ql-editor" v-html="form.content"></div>
            </el-col>
          </el-row>

          <div class="laiyuan" v-if="form.source">来源：{{ form.source }}</div>
        </el-form>
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
  listNews,
  getNews,
  delNews,
  addNews,
  updateNews,
  typeTreeNews,
  fabuNews,
  typeNews,
  submit,
  withdraw,
  recommendTop,
} from '@/api/xysystem/news'

import upLoadPic from '../../../components/upLoadPic'
import TreeSelecter from '../../../components/treeSelect/index.vue'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  name: 'News',
  components: { TreeSelecter, upLoadPic, Treeselect },
  data() {
    return {
      natureOptions: [],
      morentup: [require('@/assets/images/profile.jpg')],
      opensrc: false,
      // 控制查看弹框
      opensee: false,
      shenhe: true,
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
      },
      cover1: '',
      cover2: '',
      cover3: '',
      cover4: '',
      aastatused: '',
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
      treeSelectProps: {
        value: 'id', // ID字段名
        label: 'typeName', // 显示名称
        // children: 'children'    // 子级字段名
      },
      treeSelectOptions: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        content: [{ required: true, message: '内容不能为空', trigger: 'blur' }],
        coverType: [
          { required: true, message: '封面类型不能为空', trigger: 'blur' },
        ],
        source: [{ required: true, message: '来源不能为空', trigger: 'blur' }],
        type: [{ required: true, message: '类型不能为空', trigger: 'change' }],
        title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
        newsTypeId: [
          { required: true, message: '新闻分类不能为空', trigger: 'change' },
        ],
      },
      currentId: '', // 操作行id
      remark: '', // 撤回，取消发布备注
      visible: false, // 撤回，取消发布弹窗
      rejectType: null, // 撤回or取消发布 0:取消发布 , 1:撤回
      reason: '', // 撤回or取消发布 text
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
    this.getTypeTreeNews()
    this.xinwentype()
  },
  methods: {
    /** 查询新闻动态列表 */
    getList() {
      this.loading = true
      listNews(this.queryParams).then((response) => {
        this.newsList = response.rows
        this.newsList.forEach((element) => {
          if (element.cover !== null) {
            element.cover = element.cover.split(',')
          }
        })
        this.total = response.total
        this.loading = false
      })
    },
    // 默认图片点击
    srcsee() {
      this.opensrc = true
    },
    // 查看默认图片

    /** 转换新闻类型数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.typeName,
        children: node.children,
      }
    },
    /** 查询新闻类型（树） */
    getTypeTreeNews() {
      typeTreeNews().then((res) => {
        this.treeSelectOptions = res.data
      })
    },
    // 开关
    switchChange(n) {
      if (n.type == '图文') {
        n.type = '1'
      } else if (n.type == '音频') {
        n.type = '3'
      } else if (n.type == '视频') {
        n.type = '2'
      }
      const add = {
        status: 0,
        id: n.id,
        recommend: n.recommend,
      }
      recommendTop(add).then((response) => {
        this.$modal.msgSuccess('修改成功')
        this.getList()
      })
    },
    switchChange2(n) {
      if (n.type == '图文') {
        n.type = '1'
      } else if (n.type == '音频') {
        n.type = '3'
      } else if (n.type == '视频') {
        n.type = '2'
      }
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
    // 退回按钮
    handlertuihui(row) {
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
    // 新闻类型
    xinwentype() {
      typeNews().then((response) => {
        this.natureOptions = response.data
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 发布页面取消按钮
    canceltwo() {
      this.reset()
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
    // 表单校验
    newsTypeVali(v) {
      this.$refs.newsTypeValiRef.$emit('el.form.change', v)
    },
    // 表单重置
    reset() {
      this.form = {
        coverType: '0',
        id: null,
        newsTypeId: null,
        title: null,
        content: null,
        author: null,
        source: null,
        type: null,
        classify: null,
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
    // 查看按钮
    handlersee(row) {
      this.reset()
      const id = row.id || this.ids
      getNews(id).then((response) => {
        this.form = response.data
        if (this.form.content !== null) {
          this.form.content = this.form.content.replace(
            /<img/g,
            "<img style='max-width:90%;height:auto;'"
          )
        }
        this.opensee = true
        this.showThreeInput = true
        this.title = '预览'
      })
    },
    // 查看页确定按钮
    submitsee() {
      this.opensee = false
    },

    // 发布按钮
    handlerelease(row) {
      this.currentId = row.id
      this.aastatused = '2'
      const id = row.id || this.ids
      this.submit()
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
      this.cover1 = ''
      this.cover2 = ''
      this.cover3 = ''
      this.cover4 = ''
      this.coverType = '0'
      this.shenhe = false
      this.open = true
      this.showThreeInput = false
      this.title = '添加新闻动态'
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
      getNews(id).then((response) => {
        this.form = response.data
        this.form.content = this.form.content.replace(
          /<img/g,
          "<img style='max-width:90%;height:auto;'"
        )
        let aovss = response.data.cover
        if (aovss !== null) {
          if (this.form.coverType == '3') {
            this.cover1 = aovss.split(',')[0]
            this.cover2 = aovss.split(',')[1]
            this.cover3 = aovss.split(',')[2]
          }
          if (this.form.coverType == '1') {
            this.cover4 = aovss
          }
        }
        this.open = true
        this.showThreeInput = true
        this.title = '修改新闻动态'
      })
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
    /** 提交按钮 */
    submitForm(val) {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.coverType == '3') {
            if (!this.cover1 || !this.cover2 || !this.cover3) {
              return this.$modal.msgError('请上传三张照片')
            }
            this.form.cover =
              this.cover1 + ',' + this.cover2 + ',' + this.cover3
          } else if (this.form.coverType == '1') {
            if (!this.cover4) {
              return this.$modal.msgError('请上传一张照片')
            }
            this.form.cover = this.cover4
          } else {
            this.form.cover = ''
          }
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
                return updateNews(add)
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
                return addNews(att)
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
        })
    },
    // 发布提交
    submit() {
      const add = {
        id: this.currentId,
        status: this.aastatused,
        remark: this.rejectType == 0 ? this.remark : null,
      }
      fabuNews(add).then(() => {
        this.getList()
        if (add.status == '2') {
          this.$modal.msgSuccess('发布成功')
        } else {
          this.$modal.msgSuccess('取消发布成功')
        }
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
      this.remark = ''
      this.rejectType = null
      this.currentId = ''
      this.reason = ''
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认删除')
        .then(function () {
          return delNews(ids)
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
<style lang="scss" scoped>
.sketch_content {
  // 必须有高度 overflow 为自动
  overflow: auto;
  height: 600px;
  border-top: 1px solid #eff1f4;
  border-bottom: 1px solid #eff1f4;
  padding: 0px 0px 11px 20px;

  // 滚动条的样式,宽高分别对应横竖滚动条的尺寸
  &::-webkit-scrollbar {
    width: 3px;
  }

  // 滚动条里面默认的小方块,自定义样式
  &::-webkit-scrollbar-thumb {
    background: #8798af;
    border-radius: 2px;
  }

  // 滚动条里面的轨道
  &::-webkit-scrollbar-track {
    background: transparent;
  }
}

.morensrc {
  width: 100%;
  height: 100%;
}
.tablePic {
  height: 48px;
  width: 72px;
}
.laiyuan {
  position: relative;
  left: 15px;
}
.biaoti {
  margin-left: 10px;
  margin-bottom: 30px;
  font-size: 36px;
}
// .formll {
//   margin-top: 0px;
// }
.dialog {
  position: absolute;
}
.totol {
  color: #ffffffff;
  padding-top: 10px;
  margin: 0;
  padding-left: 10px;
  font-size: 18px;
  width: 100%;
  height: 50px;
  background-color: #006efffd;
  top: 0px;
  left: 0px;
  position: absolute;
  margin-bottom: 30px;
  .closse {
    margin-top: 5px;
    margin-right: 10px;
    float: right;
  }
}
.view-dialog ::v-deep .el-dialog__body {
  padding: 0;
}
</style>
