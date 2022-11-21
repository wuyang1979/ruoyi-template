// 政策列表
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
      <el-form-item label="政策标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入政策标题"
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
          v-hasPermi="['thinkTank:policy:add']"
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
          v-hasPermi="['thinkTank:policy:remove']"
        >删除</el-button>
      </el-col>-->

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="policyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :show-overflow-tooltip="true" />

      <el-table-column
        width="200"
        label="标题"
        align="left"
        prop="title"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handlesee(scope.row)"
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
        width="120"
        label="分类"
        align="center"
        prop="typeName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        width="120"
        label="来源"
        align="center"
        prop="source"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        width="120"
        label="作者"
        align="center"
        prop="author"
        :show-overflow-tooltip="true"
      />

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
      <el-table-column
        width="120"
        label="浏览量"
        align="center"
        prop="views"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        width="120"
        label="收藏量"
        align="center"
        prop="collects"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        width="120"
        label="点赞量"
        align="center"
        prop="favorites"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="状态" align="center" prop="statusValue"></el-table-column>

      <el-table-column
        label="操作"
        width="300"
        align="center"
        fixed="right"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['thinkTank:policy:edit']"
            v-if="
              scope.row.status == '0' ||
              scope.row.status == '1' ||
              scope.row.status == '5'
            "
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="submitFaob(scope.row)"
            v-if="
              scope.row.status == '0' ||
              scope.row.status == '1' ||
              scope.row.status == '5'
            "
            v-hasPermi="['thinkTank:policy:submit']"
          >提交审核</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleReject(scope.row)"
            v-if="scope.row.status == '2' || scope.row.status == '3'"
            v-hasPermi="['thinkTank:policy:withdraw']"
          >撤回</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlesee(scope.row)"
            v-hasPermi="['thinkTank:policy:query']"
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
            v-hasPermi="['thinkTank:policy:remove']"
          >删除</el-button>
          <el-button
            v-if="scope.row.status == '3'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handlefabu(scope.row)"
            v-hasPermi="['thinkTank:policy:release']"
          >发布</el-button>
          <el-button
            v-if="scope.row.status == '4'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUnpublish(scope.row)"
            v-hasPermi="['thinkTank:policy:cancel']"
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

    <!-- 添加或修改政策库对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="900px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" 
          maxlength="100"
                show-word-limit
          />
        </el-form-item>

        <el-form-item label="分类" prop="policyTypeId" required>
          <TreeSelecter
            :props="treeSelectProps"
            v-model="form.policyTypeId"
            :options="treeSelectOptions"
            :value="form.policyTypeId"
          ></TreeSelecter>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="来源" prop="source">
              <el-input v-model="form.source" placeholder="请输入来源" maxlength="50"
                show-word-limit/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="作者" prop="author">
              <el-input v-model="form.author" placeholder="请输入作者" 
              maxlength="50"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="封面" prop="coverType">
              <el-radio-group v-model="form.coverType" @change="changeType">
                <el-radio label="1">单图</el-radio>
                <el-radio label="3">三图</el-radio>
                <el-radio label="0">无封面</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
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
          <el-input v-model="form.introduction" type="textarea" placeholder="请输入内容"
          maxlength="500"
                show-word-limit
           />
        </el-form-item>

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

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="内容" prop="content">
              <editor v-model="form.content" :min-height="192" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" width="400" 
          maxlength="500"
                show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :disabled="shenhe" @click="submitForm('add')">提交审核</el-button>
        <el-button type="primary" @click="submitForm('att')">保存草稿</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>
    <!-- 预览页 -->
    <el-dialog
      :close-on-click-modal="false"
      :visible.sync="opensee"
      width="900px"
      :title="title"
      append-to-body
      class="dialog"
    >
      <div style="height: 70vh; overflow: auto; padding: 30px">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px" class="formll">
          <el-row>
            <div class="biaoti">{{ form.title }}</div>
          </el-row>
          <el-row>
            <el-col style="display: flex; flex-direction: row; margin-left: 15px">
              <span v-if="form.author">{{ form.author }}；</span>
              <span v-if="form.createTime">{{ form.createTime }}；</span>
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
  listPolicy,
  getPolicy,
  delPolicy,
  addPolicy,
  updatePolicy,
  fabuPolicy,
  fenleiPolicy,
  withdraw,
  submit,
  updateRecTop,
} from '@/api/xy-think-tank/policy'
// import { updetapolicyReview } from "@/api/xy-think-tank/policyReview.js";
import TreeSelecter from '../../../components/treeSelect/index.vue'
import upLoadPic from '../../../components/upLoadPic'
export default {
  name: 'Policy',
  components: { TreeSelecter, upLoadPic },
  data() {
    return {
      morentup: [require('@/assets/images/profile.jpg')],
      aastatused: '',
      shenhe: false,
      fengmian1: '',
      fengmian2: '',
      fengmian3: '',
      opensee: false,
      opensrc: false,
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
      valueid: '',
      cover1: '',
      cover2: '',
      cover3: '',
      cover4: '',
      treeSelectOptions: [],
      showThreeInput: true,
      // 表单参数
      form: {},
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
      // 政策库表格数据
      policyList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        policyTypeId: null,
        title: null,
        content: null,
        author: null,
        source: null,
        introduction: null,
        recommend: null,
        isTop: null,
        views: null,
        collects: null,
        favorites: null,
        status: null,
        approveBy: null,
        approveTime: null,
        userId: null,
        deptId: null,
        typeName: null,
      },
      treeSelectProps: {
        value: 'id', // ID字段名
        label: 'typeName', // 显示名称
        // children: 'children'    // 子级字段名
      },

      // 表单校验
      rules: {
        title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
        policyTypeId: [{ required: true, message: '分类不能为空' }],
        content: [{ required: true, message: '内容不能为空', trigger: 'blur' }],
        source: [{ required: true, message: '来源不能为空', trigger: 'blur' }],
        coverType: [
          { required: true, message: '封面类型不能为空', trigger: 'blur' },
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
    this.fenlei()
  },
  methods: {
    /** 查询政策库列表 */
    getList() {
      this.loading = true
      listPolicy(this.queryParams).then((response) => {
        this.policyList = response.rows
        this.policyList.forEach((element) => {
          if (element.cover !== null) {
            element.cover = element.cover.split(',')
          }
        })
        this.total = response.total
        this.loading = false
      })
    },
    // 查看默认图片
    srcsee() {
      this.opensrc = true
    },
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
    // 政策列表新增分类
    fenlei() {
      fenleiPolicy(this.queryParams).then((res) => {
        this.treeSelectOptions = res.data
        console.log(this.valueid, this.fenlei)
      })
    },
    handlesee2(val) {
      this.reset()
      getPolicy(val).then((response) => {
        this.form = response.data
        this.opensee = true
        this.title = '预览'
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
    // 预览按钮
    handlesee(row) {
      this.reset()
      const id = row.id || this.ids
      getPolicy(id).then((response) => {
        this.form = response.data
        if (this.form.content !== null) {
          this.form.content = this.form.content.replace(
            /<img/g,
            "<img style='max-width:90%;height:auto;'"
          )
        }
        this.opensee = true
        this.title = '预览'
      })
    },

    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    switchChange1(n) {
      const add = {
        id: n.id,
        recommend: n.recommend,
        status: 0,
      }
      updateRecTop(add).then((res) => {
        this.$modal.msgSuccess('修改成功')
        this.getList()
      })
      console.log(n, 'wwwww')
    },
    switchChange2(n) {
      const att = {
        id: n.id,
        isTop: n.isTop,
        status: 1,
      }
      updateRecTop(att).then((response) => {
        this.$modal.msgSuccess('修改成功')
        this.getList()
      })
      console.log(n, 'wwwww')
    },

    // 表单校验

    // 表单重置
    reset() {
      this.form = {
        id: null,
        policyTypeId: null,
        title: null,
        content: null,
        author: null,
        source: null,
        introduction: null,
        cover: null,
        recommend: null,
        isTop: null,
        views: null,
        collects: null,
        favorites: null,
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
      // this.$refs[form].clearValidate();
      this.title = '添加政策库'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      if (row.status == '0' || row.status == '1' || row.status == '5') {
        this.shenhe = false
      } else {
        this.shenhe = true
      }

      this.cover4 = ''
      this.cover1 = ''
      this.cover2 = ''
      this.cover3 = ''
      this.reset()
      const id = row.id || this.ids
      getPolicy(id).then((response) => {
        this.form = response.data
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
        this.title = '修改政策库'
      })
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
      fabuPolicy(add).then((response) => {
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
            }
            if (val == 'att') {
              this.form.addStatus = ''
            }
            const att = this.form
            this.$modal
              .confirm('是否确认操作？')
              .then(function () {
                return updatePolicy(att)
              })
              .then(() => {
                this.$modal.msgSuccess('修改成功')
                this.getList()
                this.open = false
              })
            // updatePolicy(this.form).then((response) => {

            //   this.getList();
            // });
          } else {
            if (val == 'add') {
              this.form.addStatus = '2'
            }
            if (val == 'att') {
              this.form.addStatus = '0'
            }
            const add = this.form
            this.$modal
              .confirm('是否确认操作？')
              .then(function () {
                return addPolicy(add)
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认删除？')
        .then(function () {
          return delPolicy(ids)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('删除成功')
        })
        .catch(() => {})
    },

    submitsee() {
      this.opensee = false
    },
  },
}
</script>
<style lang="scss" scoped>
.sketch_content {
  // 必须有高度 overflow 为自动
  overflow: auto;
  height: 434px;
  border-top: 1px solid #eff1f4;
  border-bottom: 1px solid #eff1f4;
  padding: 0px 30px 11px 27px;

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
.formll {
  margin-top: 30px;
}
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
.dialog ::v-deep .el-dialog__body {
  padding: 0;
}
</style>
