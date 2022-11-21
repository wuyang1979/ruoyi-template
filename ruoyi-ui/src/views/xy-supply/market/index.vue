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
      <el-form-item label="需求名称" prop="demandName">
        <el-input
          v-model="queryParams.demandName"
          placeholder="请输入需求名称"
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
          v-hasPermi="['supply:market:add']"
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
          v-hasPermi="['supply:market:remove']"
        >删除</el-button>
      </el-col>-->

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="marketList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        width="300"
        label="需求名称"
        align="left"
        prop="demandName"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handlesee(scope.row)"
          >{{ scope.row.demandName }}</div>
        </template>
      </el-table-column>
      <el-table-column label="分类" align="center" prop="typeName" width="120" />
      <el-table-column width="300" label="所属企业" align="left" prop="enterpriseBelong" />
      <el-table-column label="联系人" align="center" prop="contactPerson" width="120" />
      <el-table-column label="联系方式" align="center" prop="contactPhone" width="120" />
      <el-table-column label="作者" align="center" prop="auth" width="120" />
      <el-table-column label="浏览量" align="center" prop="views" width="120" />
      <el-table-column label="收藏量" align="center" prop="collects" width="120" />
      <el-table-column label="点赞量" align="center" prop="favorites" width="120" />
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
      <el-table-column width="120" label="状态" align="center" prop="statusValue" />

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
            v-if="
              scope.row.status == '0' ||
              scope.row.status == '1' ||
              scope.row.status == '5'
            "
            v-hasPermi="['supply:market:edit']"
          >修改</el-button>
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
            v-hasPermi="['supply:market:remove']"
          >删除</el-button>
          <el-button
            v-if="scope.row.status == '3'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handlefabu(scope.row)"
            v-hasPermi="['supply:market:release']"
          >发布</el-button>
          <el-button
            v-if="scope.row.status == '4'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handletuihui(scope.row)"
            v-hasPermi="['supply:market:cancel']"
          >取消发布</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleReject(scope.row)"
            v-if="scope.row.status == '2' || scope.row.status == '3'"
            v-hasPermi="['supply:market:withdraw']"
          >撤回</el-button>
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
            v-hasPermi="['supply:market:submit']"
          >提交审核</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlesee(scope.row)"
            v-hasPermi="['supply:market:query']"
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

    <!-- 添加或修改供需需求市场对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      class="view-dialog"
      width="1000px"
      append-to-body
    >
      <div
        style="padding: 30px"
        :style="[
          { height: marketdata ? '70vh' : 'auto' },
          { overflow: marketdata ? 'auto' : '' },
        ]"
      >
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="需求名称" prop="demandName">
            <el-input :disabled="marketdata" v-model="form.demandName" placeholder="请输入需求名称"      maxlength="100"
                show-word-limit/>
          </el-form-item>
          <el-form-item label="所属企业" prop="enterpriseBelongId">
            <el-select v-model="form.enterpriseBelongId" placeholder="请选择" :disabled="marketdata">
              <el-option
                v-for="item in enterpriseOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="联系人" prop="contactPerson">
                <el-input v-model="form.contactPerson" placeholder="请输入联系人"
                maxlength="50"
                show-word-limit
                 :disabled="marketdata" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系方式" prop="contactPhone">
                <el-input v-model="form.contactPhone" placeholder="请输入联系方式" :disabled="marketdata" />
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
                  :disabled="marketdata"
                ></TreeSelecter>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="作者" prop="auth">
                <el-input v-model="form.auth" 
                placeholder="请输入作者" 
                maxlength="50"
                show-word-limit
                :disabled="marketdata" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="需求简介" prop="introduce">
            <el-input
              v-model="form.introduce"
              :disabled="marketdata"
              type="textarea"
              placeholder="请输入内容"
              maxlength="500"
                show-word-limit
            />
          </el-form-item>
          <el-form-item label="需求内容" prop="content">
            <editor v-if="!marketdata" v-model="form.content" :min-height="192" />
            <div class="ql-editor" v-if="marketdata" v-html="form.content"></div>
          </el-form-item>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="置顶" prop="isTop">
                <el-switch
                  :disabled="marketdata"
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
                  :disabled="marketdata"
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
              :disabled="marketdata"
              v-model="form.remark"
              type="textarea"
              placeholder="请输入内容"
              maxlength="500"
                show-word-limit
            />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer" v-if="!marketdata">
        <el-button type="primary" :disabled="shenhe" @click="submitForm('add')">提交审核</el-button>
        <el-button type="primary" @click="submitForm('att')">保存草稿</el-button>
        <el-button @click="cancel">取 消</el-button>
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
            <el-input v-model="remark" type="textarea" :placeholder="'请输入' + reason" />
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
  listMarket,
  getMarket,
  delMarket,
  addMarket,
  updateMarket,
  releaseMarket,
  submit,
  withdraw,
  recommendTop,
} from '@/api/xy-supply/market'
import { validPhone } from '@/utils/wdcspValidate.js'
import { fenleiInfo, getEnterpriseList } from '@/api/xy-supply/info'
import TreeSelecter from '../../../components/treeSelect/index.vue'
export default {
  name: 'Market',
  components: { TreeSelecter },
  data() {
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
    return {
      enterpriseOptions: [],

      marketdata: false,
      shenhe: false,
      aastatused: '',
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
      // 供需需求市场表格数据
      marketList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      treeSelectProps: {
        value: 'id', // ID字段名
        label: 'typeName', // 显示名称
        // children: 'children'    // 子级字段名
      },
      treeSelectOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        demandName: null,
        enterpriseBelong: null,
        contactPerson: null,
        contactPhone: null,
        typeId: null,
        auth: null,
        introduce: null,
        content: null,
        recommend: null,
        isTop: null,
        status: null,
        views: null,
        collects: null,
        favorites: null,
        publishTime: null,
        userId: null,
        deptId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        enterpriseBelongId: [
          { required: true, message: '所属企业不能为空', trigger: 'change' },
        ],
        contactPerson: [
          { required: true, message: '联系人不能为空', trigger: 'blur' },
        ],
        contactPhone: [
          { required: true, message: '联系方式不能为空', trigger: 'blur' },
          { validator: checkContactPhone, trigger: 'blur' },
        ],
        demandName: [
          { required: true, message: '需求名称不能为空', trigger: 'blur' },
        ],
        typeId: [
          { required: true, message: '分类id不能为空', trigger: 'blur' },
        ],
        content: [
          { required: true, message: '需求内容不能为空', trigger: 'blur' },
        ],
        // recommend: [
        //   { required: true, message: "推荐不能为空", trigger: "blur" },
        // ],
        // isTop: [{ required: true, message: "置顶不能为空", trigger: "blur" }],
        status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
        views: [{ required: true, message: '浏览量不能为空', trigger: 'blur' }],
        collects: [
          { required: true, message: '收藏量不能为空', trigger: 'blur' },
        ],
        favorites: [
          { required: true, message: '点赞量不能为空', trigger: 'blur' },
        ],
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
    this.fenlei()
    this.loadEnterpriseList()
  },
  methods: {
    // ? 查询所有企业分类
    async loadEnterpriseList() {
      const res = await getEnterpriseList({})
      console.log('res:企业详情 ', res)
      this.enterpriseOptions = res.data
    },
    /** 查询供需需求市场列表 */
    getList() {
      this.loading = true
      listMarket(this.queryParams).then((response) => {
        this.marketList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handlesee(row) {
      this.reset()
      const id = row.id || this.ids
      getMarket(id).then((response) => {
        this.marketdata = true

        this.form = response.data
        this.open = true
        this.title = '预览'
      })
    },
    // 新增分类
    fenlei() {
      fenleiInfo(this.queryParams).then((res) => {
        this.treeSelectOptions = res.data
      })
    },
    // 发布
    handlefabu(row) {
      this.currentId = row.id
      this.aastatused = '2'
      this.submit()
    },
    // 发布撤回
    handletuihui(row) {
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
    submit() {
      const add = {
        id: this.currentId,
        status: this.aastatused,
        remark: this.rejectType == 0 ? this.remark : null,
      }
      releaseMarket(add).then((response) => {
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
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.marketdata = false
      this.form = {
        id: null,
        demandName: null,
        enterpriseBelong: null,
        contactPerson: null,
        contactPhone: null,
        typeId: null,
        auth: null,
        introduce: null,
        content: null,
        recommend: null,
        isTop: null,
        remark: null,
        status: '0',
        views: null,
        collects: null,
        favorites: null,
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
      this.title = '添加供需需求市场'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMarket(id).then((response) => {
        this.form = response.data
        console.log('this.form: ', this.form)
        this.open = true
        if (row.status == '0' || row.status == '1' || scope.row.status == '5') {
          this.shenhe = false
        } else {
          this.shenhe = true
        }
        this.title = '修改供需需求市场'
      })
    },
    /** 提交按钮 */
    submitForm(val) {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            if (val == 'add') {
              this.form.addStatus = '0'
            }
            if (val == 'att') {
              this.form.addStatus = ''
            }
            const att = this.form
            updateMarket(this.form).then((response) => {
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
            addMarket(this.form).then((response) => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    // 提交审核按钮操作
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认删除所选市场需求？')
        .then(function () {
          return delMarket(ids)
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
.view-dialog ::v-deep .el-dialog__body {
  padding: 0;
}
</style>
