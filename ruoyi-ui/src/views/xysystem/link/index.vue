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
      <el-form-item label="链接标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入链接标题"
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
          v-hasPermi="['xySystem:link:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['xySystem:link:edit']"
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
          v-hasPermi="['xySystem:link:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="linkList"
      @selection-change="handleSelectionChange"
      tooltip-effect="dark"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="图片地址"
        align="left"
        width="300"
        prop="cover"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <img class="tablePic" :src="scope.row.cover" alt />
        </template>
      </el-table-column>
      <el-table-column
        label="链接标题"
        align="left"
        width="300"
        prop="title"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="链接地址"
        align="left"
        width="300"
        prop="url"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="排序" align="center" prop="sort" />
      <el-table-column label="状态" align="center" prop="statusValue" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="180"
        fixed="right"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xySystem:link:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xySystem:link:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="noUse(scope.row)"
            v-hasPermi="['xySystem:link:status']"
          >{{ scope.row.status === "0" ? "禁用" : "启用" }}</el-button>
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

    <!-- 添加或修改友情链接对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      destroy-on-close
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="图片地址" prop="cover">
          <upLoadPic
            :cover="form.cover"
            @handleFileSuccess="handleFileSuccess"
            @handleRemove="handleRemove"
          ></upLoadPic>
        </el-form-item>
        <el-form-item label="链接标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入链接标题" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="链接地址" prop="url">
          <el-input v-model="form.url" placeholder="请输入链接地址" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number
            v-model="form.sort"
            placeholder="请输入排序"
            step-strictly
            :controls="false"
            :min="0"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { validURL } from '@/utils/validate.js'
import {
  listLink,
  addLink,
  getLink,
  updateLink,
  delLink,
  statusLink,
} from '@/api/xysystem/link'
import upLoadPic from '../../../components/upLoadPic'
export default {
  name: 'Link',
  components: { upLoadPic },
  data() {
    var checkUrl = (rule, value, callback) => {
      if (!value) {
        return callback()
      }
      setTimeout(() => {
        if (!validURL(value)) {
          callback(new Error('请输入合法地址'))
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
      // 友情链接表格数据
      linkList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cover: null,
        title: null,
        url: null,
        sort: null,
        type: null,
        position: null,
        status: null,
      },
      positionOptions: [
        {
          value: '0',
          label: '中部',
        },
        {
          value: '1',
          label: '底部',
        },
      ],
      // 表单参数
      form: {
        position: '0',
      },
      // 表单校验
      rules: {
        position: [
          { required: true, message: '请选择位置', trigger: 'change' },
        ],
        cover: [{ required: true, message: '请上传图片' }],
        title: [{ required: true, message: '请填写链接标题', trigger: 'blur' }],
        sort: [{ required: true, message: '请填写排序', trigger: 'blur' }],
        url: [
          { required: true, message: '请填写链接地址', trigger: 'blur' },
          { validator: checkUrl, trigger: 'blur' },
        ],
      },
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询友情链接列表 */
    getList() {
      this.loading = true
      listLink(this.queryParams).then((response) => {
        this.linkList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 图片删除
    handleRemove() {
      this.form.cover = ''
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        cover: '',
        title: null,
        url: null,
        sort: null,
        type: null,
        position: '0',
        status: '0',
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
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
      this.title = '添加友情链接'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getLink(id).then((response) => {
        this.form = response.data
        this.open = true
        this.title = '修改友情链接'
      })
    },
    // 禁用启用
    noUse(n) {
      statusLink(n).then((res) => {
        if (res.code === 200) {
          this.$modal.msgSuccess('修改状态成功')
          this.getList()
        }
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateLink(this.form).then((response) => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addLink(this.form).then((response) => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleFileSuccess(file) {
      this.form.cover = file.src
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认删除友情链接编号为"' + ids + '"的数据项？')
        .then(function () {
          return delLink(ids)
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
.bigUpload::v-deep {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80px !important;
  width: 80px !important;
  font-size: 28px !important;
  border: 1px solid #ccc;
}
.showImg {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80px !important;
  width: 80px !important;
  position: relative;
  cursor: pointer;
  .delImg {
    position: absolute;
    right: -10px;
    top: -10px;
    height: 20px;
    width: 20px;
    background-color: red;
    font-size: 16px;
    color: #fff;
    line-height: 20px;
    text-align: center;
    border-radius: 10px;
    cursor: pointer;
  }
}
.showBigImg {
  height: 600px;
  width: 800px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.tablePic {
  height: 40px;
  width: 70px;
}
</style>
