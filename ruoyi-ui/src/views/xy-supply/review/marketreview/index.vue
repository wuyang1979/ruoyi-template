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
        <el-button
          :disabled="single"
          type="success"
          icon="el-icon-refresh"
          size="mini"
          v-hasPermi="['supply:market:batch']"
          @click="resetparam('add0')"
        >批量通过</el-button>
        <!-- <el-button
          :disabled="single"
          type="danger"
          icon="el-icon-refresh"
          size="mini"
          v-hasPermi="['supply:market:batch']"
          @click="resetparam('att1')"
        >批量驳回</el-button>-->
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
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
            @click="handleUpdate(scope.row)"
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
        <template slot-scope="scope">{{
          scope.row.recommend === "0" ? "否" : "是"
        }}</template>
      </el-table-column>
      <el-table-column width="120" label="置顶" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">{{
          scope.row.isTop === "0" ? "否" : "是"
        }}</template>
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
            icon="el-icon-check"
            @click="handletongguo(scope.row)"
            v-hasPermi="['supply:market:approval']"
          >审核通过</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="markettuihui(scope.row)"
            v-hasPermi="['supply:market:approval']"
          >审核不通过</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleUpdate(scope.row)"
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
      width="1000px"
      append-to-body
      class="dialog"
    >
      <div style="height: 70vh;overflow:auto;padding: 30px">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="需求名称" prop="demandName">
            <el-input :disabled="marketdata" v-model="form.demandName" placeholder="请输入需求名称" />
          </el-form-item>
          <el-form-item label="所属企业" prop="enterpriseBelong">
            <el-input v-model="form.enterpriseBelong" placeholder="请输入所属企业" :disabled="marketdata" />
          </el-form-item>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="联系人" prop="contactPerson">
                <el-input v-model="form.contactPerson" placeholder="请输入联系人" :disabled="marketdata" />
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
              <el-form-item label="分类" prop="typeName">
                <el-input v-model="form.typeName" placeholder="请输入分类" :disabled="marketdata" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="作者" prop="auth">
                <el-input v-model="form.auth" placeholder="请输入作者" :disabled="marketdata" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="需求简介" prop="introduce">
            <el-input
              v-model="form.introduce"
              :disabled="marketdata"
              type="textarea"
              placeholder="请输入内容"
            />
          </el-form-item>
          <el-form-item label="需求内容">
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
              maxlength="500" show-word-limit
            />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer" v-if="!marketdata">
        <el-button type="primary" :disabled="shenhe" @click="submitForm('add')">提交审核</el-button>
        <el-button type="primary" @click="submitForm('att')">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 驳回原因 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="opensee"
      width="500px"
      append-to-body
    >
      <el-row :gutter="20">
        <el-form :span="12" ref="form" :model="form" :rules="rules">
          <el-form-item label="驳回原因" prop="remark">
            <el-input v-model="remark" type="textarea" placeholder="请输入内容" />
          </el-form-item>
        </el-form>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm('att')">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listMarket,
  getMarket,
  batchMarket,
  reviewMarket,
} from '@/api/xy-supply/review/marketreview'

export default {
  name: 'Market',
  data() {
    return {
      marketdata: true,
      shenhe: false,
      aastatused: '',
      remark: '',
      idee: '',
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
      opensee: false,
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
        demandName: [
          { required: true, message: '需求名称不能为空', trigger: 'blur' },
        ],
        typeId: [
          { required: true, message: '分类id不能为空', trigger: 'blur' },
        ],
        content: [
          { required: true, message: '需求内容不能为空', trigger: 'blur' },
        ],
        recommend: [
          { required: true, message: '推荐不能为空', trigger: 'blur' },
        ],
        isTop: [{ required: true, message: '置顶不能为空', trigger: 'blur' }],
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
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询供需需求市场列表 */
    getList() {
      this.loading = true
      listMarket(this.queryParams).then((response) => {
        this.marketList = response.rows
        this.total = response.total
        this.loading = false
      })
    },

    // 取消按钮
    cancel() {
      this.opensee = false
      this.reset()
    },
    // 表单重置
    reset() {
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
    // switchChange1(n) {
    //   const add = {
    //     id: n.id,
    //     recommend: n.recommend,
    //   };
    //   updateMarket(add).then((res) => {
    //     this.$modal.msgSuccess("修改成功");
    //     this.getList();
    //   });
    //   console.log(n, "wwwww");
    // },
    // switchChange2(n) {
    //   const att = {
    //     id: n.id,
    //     isTop: n.isTop,
    //   };
    //   updateMarket(att).then((response) => {
    //     this.$modal.msgSuccess("修改成功");
    //     this.getList();
    //   });
    //   console.log(n, "wwwww");
    // },
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
        this.open = true
        this.title = '预览'
      })
    },
    // 驳回按钮操作
    markettuihui(row) {
      this.idee = row.id
      this.opensee = true
      this.title = '驳回原因'
    },
    // 批量通过/驳回
    resetparam(val) {
      let abb = ''
      if (val == 'add0') {
        abb = '0'
      } else {
        abb = '1'
      }
      const add = {
        ids: this.ids,
        status: abb,
      }
      this.$modal
        .confirm('是否确认操作')
        .then(function () {
          return batchMarket(add)
        })
        .then(() => {
          this.$modal.msgSuccess('审核成功')
          this.getList()
          this.single = true
        })
    },
    /** 通过按钮操作 */
    handletongguo(row) {
      const add = {
        status: '2',
        remark: this.remark,
        id: row.id,
      }
      this.$modal
        .confirm('是否确认通过')
        .then(function () {
          return reviewMarket(add)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('审核成功')
        })
        .catch(() => {})
    },
    /** 提交按钮 */
    submitForm(val) {
      const add = {
        status: '1',
        remark: this.remark,
        id: this.idee,
      }
      reviewMarket(add).then(() => {
        this.getList()
        this.$modal.msgSuccess('审核成功')
        this.opensee = false
        this.remark = ''
      })
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认删除所选市场需求订单？')
        .then(function () {
          return delMarket(ids)
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
        'supply/market/export',
        {
          ...this.queryParams,
        },
        `market_${new Date().getTime()}.xlsx`
      )
    },
  },
}
</script>
<style lang="scss" scoped>
.dialog ::v-deep .el-dialog__body {
  padding: 0;
}
</style>
