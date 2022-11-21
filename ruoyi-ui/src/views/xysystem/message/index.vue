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
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
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
          v-hasPermi="['xySystem:message:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['xySystem:message:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="messageList" @selection-change="handleSelectionChange">
      <el-table-column
        class="el-column"
        type="selection"
        width="55"
        align="center"
        style="width: 300px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        class="el-column"
        label="用户类型"
        align="center"
        prop="userType"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        class="el-column"
        label="消息标题"
        align="center"
        prop="title"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        class="el-column"
        label="消息内容"
        align="center"
        prop="content"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        class="el-column"
        label="消息类型"
        align="center"
        prop="msgType"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column
        class="el-column"
        label="消息状态"
        align="center"
        prop="isRead"
        :show-overflow-tooltip="true"
      />-->
      <el-table-column
        class="el-column"
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
            v-hasPermi="['xySystem:message:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xySystem:message:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleSee(scope.row)"
            v-hasPermi="['xySystem:message:query']"
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

    <!-- 添加或修改消息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="550px"
      append-to-body
    >
      <el-form ref="userForm" :model="userForm" :rules="userRules" label-width="80px">
        <el-form-item label="消息标题" prop="title">
          <el-input
            clearable
            placeholder="请输入标题"
            :disabled="readOnly"
            v-model="userForm.title"
            maxlength="100"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item label="消息内容" prop="content">
          <el-input
            clearable
            type="textarea"
            :rows="3"
            :disabled="readOnly"
            placeholder="请输入内容"
            v-model="userForm.content"
            maxlength="500"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item label="消息类型" prop="msgType">
          <el-select v-model="userForm.msgType" :disabled="readOnly" placeholder="请选择消息类型">
            <el-option
              v-for="item in msgOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="人员类型" prop="userType">
          <el-radio-group v-model="userForm.userType" :disabled="readOnly" @change="radioChange">
            <el-radio label="0">全部</el-radio>
            <el-radio label="1">专家</el-radio>
            <el-radio label="2">企业</el-radio>
            <el-radio label="3">供应商</el-radio>
            <el-radio label="4">指定用户</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <el-input
        v-show="userTableShow"
        clearable
        placeholder="请输入用户名或者手机号"
        v-model="queryuserParams.searchValue"
        class="input-with-select"
      >
        <el-button slot="append" icon="el-icon-search" @click="searchUserListFn"></el-button>
      </el-input>
      <el-table
        v-show="userTableShow"
        ref="multipleTable"
        :data="userListTableData"
        style="width: 100%; max-height:200px; overflow-y: auto"
        @selection-change="handleSelectUserChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="userName" label="用户名"></el-table-column>
        <el-table-column prop="phonenumber" label="电话" width="140"></el-table-column>
      </el-table>
      <pagination
        v-show="userTableShow && userTotal > 0"
        :total="userTotal"
        :page.sync="queryuserParams.pageNum"
        :limit.sync="queryuserParams.pageSize"
        @pagination="searchUserListFn"
      />

      <div slot="footer" class="dialog-footer" v-show="!readOnly">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listMessage,
  addMessage,
  updateMessage,
  optionList,
  getMessage,
  delMessage,
} from '@/api/xysystem/message'

export default {
  name: 'Message',
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
      // 消息表格数据
      messageList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        content: null,
        msgType: null,
        status: null,
      },
      queryuserParams: {
        searchValue: null,
        pageNum: 1,
        pageSize: 10,
      },
      userTotal: 0,
      // 所有用户
      userListTableData: [],
      // 表单参数
      textarea: '',
      // 表单校验
      userRules: {
        title: [{ required: true, message: '请填写消息标题', trigger: 'blur' }],
        content: [
          { required: true, message: '请填写消息内容', trigger: 'blur' },
        ],
        msgType: [
          { required: true, message: '请填写消息类型', trigger: 'blur' },
        ],
        userType: [
          { required: true, message: '请填写用户类型', trigger: 'blur' },
        ],
      },
      userForm: {
        useIds: [],
        title: '',
        msgType: 0,
        userType: 0,
        content: '',
      },
      msgOptions: [
        {
          value: '0',
          label: '系统消息',
        },
        {
          value: '1',
          label: '审核消息',
        },
      ],
      userTableShow: false,
      readOnly: false, // 新增或预览
    }
  },
  created() {
    this.getList()
    this.searchUserListFn()
  },
  methods: {
    /** 查询消息列表 */
    getList() {
      this.loading = true
      listMessage(this.queryParams).then((response) => {
        this.messageList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    restUserForm() {
      this.userForm = {
        useIds: [],
        title: '',
        msgType: 0,
        userType: 0,
        content: '',
      }
    },
    // 表单重置
    reset() {
      this.userForm = {
        id: null,
        title: null,
        content: null,
        msgType: null,
        status: '0',
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
      }
      this.resetForm('userForm')
      this.queryuserParams = {
        searchValue: null,
        pageNum: 1,
        pageSize: 10,
      }
      this.userTableShow = false
      this.readOnly = false
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
    radioChange(v) {
      if (v === '4') {
        this.userTableShow = true
      } else {
        this.userTableShow = false
      }
    },
    // 用户列表多选框选中数据
    handleSelectUserChange(selection) {
      console.log(selection, 'selection')
      this.userForm.useIds = selection.map((item) => item.userId)
    },
    // 模糊查询用户
    searchUserListFn() {
      optionList(this.queryuserParams).then((res) => {
        this.userTotal = res.total
        this.userListTableData = res.rows
      })
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加消息'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMessage(id).then((response) => {
        this.userForm = response.data
        this.open = true
        this.title = '修改消息'
        if (response.data.userType == '4') {
          this.radioChange(response.data.userType)
        }
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if (this.userForm.userType === '4') {
            if (this.userForm.useIds && this.userForm.useIds.length > 0) {
              let method = null
              if (this.userForm.id != null) {
                method = updateMessage
              } else {
                method = addMessage
              }
              method(this.userForm).then((response) => {
                this.$modal.msgSuccess('发送消息成功')
                this.$refs['userForm'].resetFields()
                this.open = false
                this.restUserForm()
                this.userTableShow = false
                this.getList()
              })
            } else {
              this.$modal.msgWarning('请选择发送用户！')
            }
          } else {
            let method = null
            if (this.userForm.id != null) {
              method = updateMessage
            } else {
              method = addMessage
            }
            this.userForm.useIds = []
            method(this.userForm).then((response) => {
              this.$modal.msgSuccess('发送消息成功')
              this.$refs['userForm'].resetFields()
              this.open = false
              this.restUserForm()
              this.userTableShow = false
              this.getList()
            })
          }
          // if (this.userForm.id != null) {
          //   updateMessage(this.userForm).then(response => {
          //     this.$modal.msgSuccess("修改成功");
          //     this.open = false;
          //     this.getList();
          //   });
          // } else {
          //   addMessage(this.userForm).then(response => {
          //     this.$modal.msgSuccess("发送消息成功");
          //     this.$refs["userForm"].resetFields();
          //     this.open = false;
          //     this.getList();
          //   });
          // }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认删除消息编号为"' + ids + '"的数据项？')
        .then(function () {
          return delMessage(ids)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('删除成功')
        })
        .catch(() => {})
    },
    handleSee(row) {
      this.reset()
      const id = row.id || this.ids
      getMessage(id).then((response) => {
        this.userForm = response.data
        this.open = true
        this.title = '预览'
        this.readOnly = true
      })
    },
  },
}
</script>
<style scoped>
.el-column {
  width: 200px !important;
}
.el-radio {
  margin-right: 20px !important;
}
</style>
