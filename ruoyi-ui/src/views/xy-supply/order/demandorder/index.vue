<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="120px"
    >
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="意向企业名称" prop="enterpriseName">
        <el-input
          v-model="queryParams.enterpriseName"
          placeholder="请输入意向企业名称"
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['supply:demandOrder:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单编号" width="300" align="left" prop="orderNo">
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handleUpdate(scope.row)"
          >{{ scope.row.orderNo }}</div>
        </template>
      </el-table-column>
      <el-table-column label="意向企业" width="180" align="center" prop="enterpriseName" />
      <el-table-column
        label="联系人"
        width="180"
        align="center"
        prop="enterpriseContactPerson"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="联系方式"
        width="180"
        align="center"
        prop="enterpriseContactPhone"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column label="需求内容" width="300" align="left" prop="content">
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handleUpdate(scope.row)"
          >{{ scope.row.content }}</div>
        </template>
      </el-table-column> -->
      <el-table-column
        label="分类"
        :show-overflow-tooltip="true"
        width="180"
        align="center"
        prop="typeName"
      />
      <el-table-column
        label="创建时间"
        :show-overflow-tooltip="true"
        width="180"
        align="center"
        prop="createTime"
      />

      <el-table-column
        label="操作"
        align="center"
        width="180"
        fixed="right"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['supply:demandOrder:query']"
          >预览</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['supply:demandOrder:remove']"
          >删除</el-button>
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

    <!-- 添加或修改供需订单对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="1000px"
      append-to-body
    >
      <div
        style="padding: 30px"
        :style="[{height:orderdasbord?'70vh':'auto'},{overflow:orderdasbord?'auto':''}]"
      >
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-form-item label="意向企业名称" prop="enterpriseName">
            <el-input
              :disabled="orderdasbord"
              v-model="form.enterpriseName"
              placeholder="请输入意向企业名称"
            />
          </el-form-item>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="联系人" prop="enterpriseContactPerson">
                <el-input
                  :disabled="orderdasbord"
                  v-model="form.enterpriseContactPerson"
                  placeholder="请输入联系人"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :disabled="orderdasbord" label="联系方式" prop="enterpriseContactPhone">
                <el-input
                  :disabled="orderdasbord"
                  v-model="form.enterpriseContactPhone"
                  placeholder="请输入联系方式"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-divider content-position="right"></el-divider>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="订单编号" prop="orderNo">
                <el-input v-model="form.orderNo" :disabled="orderdasbord" placeholder="请输入订单编号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="创建时间" prop="createTime">
                <el-input v-model="form.createTime" :disabled="orderdasbord" placeholder="请输入" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="需求名称" prop="demandName">
            <el-input v-model="form.productName" :disabled="orderdasbord" placeholder="请输入需求名称" />
          </el-form-item>
          <el-form-item label="发布企业" prop="enterpriseBelong">
            <el-input
              :disabled="orderdasbord"
              v-model="form.enterpriseBelong"
              placeholder="请输入发布企业"
            />
          </el-form-item>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="联系人" :disabled="orderdasbord" prop="contactPerson">
                <el-input
                  :disabled="orderdasbord"
                  v-model="form.contactPerson"
                  placeholder="请输入联系人"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系方式" prop="contactPhone">
                <el-input
                  :disabled="orderdasbord"
                  v-model="form.contactPhone"
                  placeholder="请输入联系方式"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="分类" prop="typeName">
                <el-input v-model="form.typeName" :disabled="orderdasbord" placeholder="请输入分类" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="作者" prop="auth">
                <el-input v-model="form.auth" :disabled="orderdasbord" placeholder="请输入作者" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listDemandOrder,
  getDemandOrder,
  delDemandOrder,
  addDemandOrder,
  updateDemandOrder,
} from '@/api/xy-supply/order/demandOrder'

export default {
  name: 'Order',
  data() {
    return {
      orderdasbord: true,
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
      // 供需订单表格数据
      orderList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        enterpriseName: null,
        enterpriseContactPerson: null,
        enterpriseContactPhone: null,
        productName: null,
        enterpriseBelong: null,
        contactPerson: null,
        contactPhone: null,
        typeId: null,
        auth: null,
        platformPrice: null,
        marketPrice: null,
        handleCode: null,
        handleQrUrl: null,
        cover: null,
        videoUrl: null,
        orderType: null,
        status: null,
        userId: null,
        deptId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderNo: [
          { required: true, message: '订单编号不能为空', trigger: 'blur' },
        ],
        productName: [
          { required: true, message: '产品名称不能为空', trigger: 'blur' },
        ],
        typeId: [
          { required: true, message: '分类id不能为空', trigger: 'blur' },
        ],
        handleCode: [
          { required: true, message: 'Handle码不能为空', trigger: 'blur' },
        ],
        orderType: [
          { required: true, message: '订单类型不能为空', trigger: 'change' },
        ],
        status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
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
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询供需订单列表 */
    getList() {
      this.loading = true
      listDemandOrder(this.queryParams).then((response) => {
        this.orderList = response.rows
        this.total = response.total
        this.loading = false
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
        orderNo: null,
        enterpriseName: null,
        enterpriseContactPerson: null,
        enterpriseContactPhone: null,
        productName: null,
        enterpriseBelong: null,
        contactPerson: null,
        contactPhone: null,
        typeId: null,
        auth: null,
        platformPrice: null,
        marketPrice: null,
        handleCode: null,
        handleQrUrl: null,
        cover: null,
        videoUrl: null,
        orderType: null,
        status: '0',
        remark: null,
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getDemandOrder(id).then((response) => {
        this.form = response.data
        this.open = true
        this.title = '预览'
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认所选需求订单？')
        .then(function () {
          return delDemandOrder(ids)
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
::v-deep .el-dialog__body {
  padding: 0;
}
</style>
