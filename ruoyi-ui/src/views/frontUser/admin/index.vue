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
      <el-form-item label="名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账号" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="userType">
        <TreeSelecter
          :props="treeSelectProps"
          v-model="queryParams.userType"
          :options="typeLists"
          :value="queryParams.userType"
        ></TreeSelecter>
      </el-form-item>
      <el-form-item label="联系方式" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入联系方式"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账号状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择账号状态" clearable>
          <el-option label="全部" value></el-option>
          <el-option
            v-for="item in dict.type.xy_friend_link_status"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="审批状态" prop="approveStatus">
        <el-select v-model="queryParams.approveStatus" placeholder="请选择审批状态" clearable>
          <el-option label="全部" value></el-option>
          <el-option
            v-for="item in dict.type.xy_user_approval_status"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="adminList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="名称" align="center" prop="userName" width="180">
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handlesee(scope.row)"
          >{{ scope.row.userName }}</div>
        </template>
      </el-table-column>
      <el-table-column label="账号" align="center" prop="phonenumber" width="180" />
      <el-table-column label="类型" align="center" prop="userTypeName" width="120" />
      <el-table-column label="联系方式" align="center" prop="phonenumber" width="180" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="审核时间" align="center" prop="approveTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.approveTime ? scope.row.approveTime : '- -' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="帐号状态" align="center" prop="status" width="120">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            @change="statusChange(scope.row)"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="0"
            inactive-value="1"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="审批状态" align="center" prop="approveStatusValue" width="120" />
      <el-table-column
        label="操作"
        align="center"
        width="300px"
        fixed="right"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlesee(scope.row)"
            v-hasPermi="['frontUser:admin:query']"
          >预览</el-button>
          <el-button
            v-if="scope.row.status == '0' && scope.row.approveStatus == '0'"
            size="mini"
            type="text"
            icon="el-icon-check"
            v-hasPermi="['frontUser:admin:approval']"
            @click="handleAgree(scope.row)"
          >通过</el-button>
          <el-button
            v-if="scope.row.status == '0' && scope.row.approveStatus == '0'"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleReject(scope.row)"
            v-hasPermi="['frontUser:admin:approval']"
          >退回</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-key"
            @click="handleResetPwd(scope.row)"
            v-hasPermi="['frontUser:admin:edit']"
          >重置密码</el-button>
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

    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="opensee"
      v-if="opensee"
      class="view-dialog"
      width="1000px"
      append-to-body
    >
      <div style="height: 70vh;overflow: auto;padding: 30px;">
        <expert-preview :form="form" v-if="previewType == 1" />
        <business-preview :form="form" v-else-if="previewType == 2" />
        <user-preview :form="form" v-else />
      </div>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="rejectDialog"
      v-if="rejectDialog"
      width="500px"
      append-to-body
    >
      <el-row :gutter="20">
        <el-form :span="12">
          <el-form-item label="驳回原因">
            <el-input v-model="rejectForm.remark" type="textarea" placeholder="请输入驳回原因" />
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
  listAdmin,
  getAdmin,
  delAdmin,
  addAdmin,
  updateAdmin,
  typeList,
  approval,
} from '@/api/frontUser/admin'
import TreeSelecter from '@/components/treeSelect/index.vue'
import businessPreview from './businessPreview.vue'
import userPreview from './userPreview.vue'
import expertPreview from './expertPreview.vue'

export default {
  name: 'Admin',
  components: { TreeSelecter, businessPreview, userPreview, expertPreview },
  dicts: ['xy_friend_link_status', 'xy_user_approval_status'],
  data() {
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
      // 前台用户管理表格数据
      adminList: [],
      // 用户类型
      typeLists: [],
      // 弹出层标题
      title: '',
      // 预览判断条件
      opensee: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deptId: null,
        enterpriseId: null,
        userName: null,
        phonenumber: null,
        password: null,
        realName: null,
        college: null,
        nativePlace: null,
        political: null,
        idType: null,
        idNo: null,
        education: null,
        status: null,
        approveStatus: null,
        userType: null,
        loginIp: null,
        loginDate: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        idType: [
          { required: true, message: '证件类型不能为空', trigger: 'change' },
        ],
        approveStatus: [
          { required: true, message: '审批状态不能为空', trigger: 'blur' },
        ],
      },
      treeSelectProps: {
        value: 'id', // ID字段名
        label: 'typeName', // 显示名称
        // children: 'children'    // 子级字段名
      },
      rejectDialog: false,
      rejectForm: {},
      // 预览类型 1:专家 2:企业,服务商 3:会员
      previewType: null,
    }
  },
  created() {
    this.getTypeList()
    this.getList()
  },
  methods: {
    /** 查询前台用户类型 */
    getTypeList() {
      typeList().then((response) => {
        this.typeLists = response.data
        this.typeLists.unshift({ parentId: 0, id: 0, typeName: '全部' })
      })
    },
    /** 查询前台用户管理列表 */
    getList() {
      this.loading = true
      let param = { ...this.queryParams }
      param.userType = param.userType ? param.userType : ''
      listAdmin(param).then((response) => {
        this.adminList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 表单重置
    reset() {
      this.form = {
        userId: null,
        deptId: null,
        enterpriseId: null,
        userName: null,
        phonenumber: null,
        password: null,
        realName: null,
        college: null,
        nativePlace: null,
        political: null,
        idType: null,
        idNo: null,
        education: null,
        status: '0',
        approveStatus: '0',
        userType: null,
        delFlag: null,
        loginIp: null,
        loginDate: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
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
      this.ids = selection.map((item) => item.userId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    statusChange(row) {
      const param = {
        userId: row.userId,
        status: row.status,
      }
      updateAdmin(param).then((res) => {
        this.$modal.msgSuccess('修改成功')
        this.getList()
      })
    },
    // 预览
    handlesee(row) {
      this.reset()
      const userId = row.userId
      this.previewType =
        row.userType == 1 ? 1 : row.userType == 2 || row.userType == 3 ? 2 : 3
      getAdmin(userId).then((response) => {
        if (response.data && response.data.data) {
          this.form = response.data.data
          this.opensee = true
          this.title = '预览'
        } else {
          this.$modal.msgWarning('暂无数据')
        }
      })
    },
    // 通过
    handleAgree(row) {
      this.$modal
        .confirm('是否确认通过')
        .then(function () {
          return approval({
            approveStatus: '2',
            id: row.userId,
            userType: row.userType,
            oldUserType: row.oldUserType,
          })
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('审核成功')
        })
        .catch(() => {})
    },
    // 退回
    handleReject(row) {
      let { userId, userType } = row
      this.rejectForm = {
        approveStatus: '1',
        id: userId,
        userType: userType,
        oldUserType: row.oldUserType,
        remark: '',
      }
      this.rejectDialog = true
      this.title = '退回'
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.realName + '"的新密码', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        closeOnClickModal: false,
        inputPattern:
          /^(?=.*\d)(?=.*?[a-z])(?=.*?[A-Z])(?=.*[~!@#$%^&*])[\da-zA-Z~!@#$%^&*]{6,18}$/,
        inputErrorMessage: '8-16字符，要包含大小写字母、数字以及特殊符号',
      })
        .then(({ value }) => {
          updateAdmin({ userId: row.userId, password: value }).then(
            (response) => {
              this.$modal.msgSuccess('修改成功，新密码是：' + value)
            }
          )
        })
        .catch(() => {})
    },
    submitReject() {
      approval(this.rejectForm).then((res) => {
        this.$modal.msgSuccess('审核成功')
        this.rejectDialog = false
        this.getList()
      })
    },
    cancelReject() {
      this.rejectDialog = false
    },
  },
}
</script>
<style lang="scss" scoped>
.view-dialog ::v-deep .el-dialog__body {
  padding: 0;
}
</style>
