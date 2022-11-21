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
      <el-form-item label="产品名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入产品名称"
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
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
        <el-button
          :disabled="single"
          type="success"
          icon="el-icon-refresh"
          size="mini"
          v-hasPermi="['supply:info:batch']"
          @click="resetparam('add0')"
          >批量通过</el-button
        >
        <!-- <el-button
          :disabled="single"
          type="danger"
          icon="el-icon-refresh"
          size="mini"
          v-hasPermi="['supply:info:batch']"
          @click="resetparam('att1')"
        >批量驳回</el-button>-->
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="infoList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column
        width="300"
        label="产品名称"
        align="left"
        prop="name"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handlesee(scope.row)"
          >
            {{ scope.row.name }}
          </div>
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
            @click="handleimg(scope.row)"
          >
            {{ scope.row.handleCode }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="封面" width="120" align="center" prop="cover">
        <template slot-scope="scope">
          <div v-if="scope.row.cover == null || scope.row.cover == ''">
            <el-image
              class="tablePic"
              :src="morentup[0]"
              :preview-src-list="morentup"
              alt
            />
          </div>
          <div v-else>
            <el-image
              class="tablePic"
              :src="scope.row.cover[0]"
              :preview-src-list="scope.row.cover"
              alt
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="视频" width="120" align="center" prop="videoUrl">
        <template slot-scope="scope">
          <div style="width: 200px; height: 60px">
            <video :src="scope.row.videoUrl"></video>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="分类"
        width="120"
        align="center"
        prop="typeName"
      />
      <el-table-column
        label="平台价(元)"
        width="120"
        align="center"
        prop="platformPrice"
      />
      <el-table-column
        label="市场价(元)"
        width="120"
        align="center"
        prop="marketPrice"
      />
      <el-table-column
        label="所属企业"
        align="left"
        width="300"
        prop="enterpriseBelong"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        width="120"
        label="联系人"
        align="center"
        prop="contactPerson"
      />
      <el-table-column
        width="120"
        label="联系方式"
        align="center"
        prop="contactPhone"
      />
      <el-table-column width="120" label="作者" align="center" prop="auth" />
      <el-table-column width="120" label="浏览量" align="center" prop="views" />
      <el-table-column
        width="120"
        label="收藏量"
        align="center"
        prop="collects"
      />
      <el-table-column
        width="120"
        label="点赞量"
        align="center"
        prop="favorites"
      />
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
      <el-table-column
        width="120"
        label="置顶"
        align="center"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">{{
          scope.row.isTop === "0" ? "否" : "是"
        }}</template>
      </el-table-column>
      <el-table-column
        width="120"
        label="状态"
        align="center"
        prop="statusValue"
      />

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
            icon="el-icon-check"
            @click="handletongguo(scope.row)"
            v-hasPermi="['supply:info:approval']"
            >审核通过</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['supply:info:approval']"
            >审核不通过</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlesee(scope.row)"
            v-hasPermi="['supply:info:query']"
            >预览</el-button
          >
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

    <!-- 添加或修改供需产品信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
    >
      <el-row :gutter="20">
        <el-form :span="12" ref="form" :model="form" :rules="rules">
          <el-form-item label="驳回原因" prop="remark">
            <el-input
              v-model="remark"
              type="textarea"
              placeholder="请输入内容"
            />
          </el-form-item>
        </el-form>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit('att')">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      class="view-dialog"
      :visible.sync="opensee"
      width="1000px"
      append-to-body
    >
      <div style="height: 70vh; overflow: auto; padding: 30px">
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <div class="bigbox">
            <div class="images">
              <el-carousel
                class="lun_imgs"
                v-if="imgs && imgs.length > 0"
                ref="carousel"
                @click.native="linkTo"
              >
                <el-carousel-item
                  class="lun_img"
                  v-for="item in imgs"
                  v-bind:key="item"
                >
                  <el-image :src="item" style="width: 100%; height: 100%" />
                </el-carousel-item>
              </el-carousel>
              <el-carousel v-else class="lun_imgs">
                <el-carousel-item
                  class="lun_img"
                  v-for="item in morentup"
                  v-bind:key="item"
                >
                  <el-image :src="item" style="width: 100%; height: 100%" />
                </el-carousel-item>
              </el-carousel>
            </div>
            <div class="names">
              <div class="seename">{{ form.name }}</div>
              <div style="margin-top: 10px">{{ form.handleCode }}</div>
              <div v-if="form.handleQrUrl">
                <img
                  style="width: 100px; height: 100px"
                  :src="form.handleQrUrl"
                  alt
                />
              </div>
              <div class="marketpriced">￥{{ form.marketPrice }}</div>
              <div style="margin-top: 10px; margin-bottom: 10px">
                {{ form.enterpriseBelong }}
              </div>
              <div style="font-size: 20px">
                <span>{{ form.contactPerson }}:</span>
                <span>{{ form.contactPhone }}</span>
              </div>
            </div>
          </div>
          <div class="introduce">详情介绍：</div>
          <!-- <div class="ql-editor" v-html="form.introduce"></div> -->
          <div class="ql-editor" v-html="form.content"></div>
        </el-form>
      </div>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :visible.sync="handlemaimg"
      append-to-body
      width="500px"
      center
      title="二维码查看"
    >
      <img :src="handleSrc" alt width="100%" />
    </el-dialog>
  </div>
</template>

<script>
import {
  listInfo,
  getInfo,
  updetareinfo,
  updetainfois,
} from "@/api/xy-supply/review/handlereview";
import TreeSelecter from "../../../../components/treeSelect/index.vue";
import upLoadPic from "../../../../components/upLoadPic";
export default {
  name: "Info",
  components: { TreeSelecter, upLoadPic },
  data() {
    return {
      handlemaimg: false,
      handleSrc: "",
      dialogImageUrl: "",
      dialogVisible: false,
      disabled: false,
      morentup: [require("@/assets/images/profile.jpg")],
      //图片上传组件的headers请求头对象
      headerObj: {
        Authorization: window.sessionStorage.getItem("token"),
      },
      //控制图片预览窗口的显示与隐藏
      previewVisible: false,
      addForm: [],
      //图片地址
      previewPath: "",

      // 图片轮回
      imgs: [],

      // 遮罩层
      loading: true,
      // 预览判断条件
      opensee: false,
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
      // 供需产品信息表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      remark: "",
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        enterpriseBelong: null,
        contactPerson: null,
        contactPhone: null,
        typeId: null,
        auth: null,
        platformPrice: null,
        marketPrice: null,
        handleCode: null,
        cover: null,
        videoUrl: null,
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
        name: [
          { required: true, message: "产品名称不能为空", trigger: "blur" },
        ],
        typeId: [
          { required: true, message: "分类id不能为空", trigger: "blur" },
        ],
        handleCode: [
          { required: true, message: "Handle码不能为空", trigger: "blur" },
        ],
        content: [
          { required: true, message: "产品内容不能为空", trigger: "blur" },
        ],
        recommend: [
          { required: true, message: "推荐不能为空", trigger: "blur" },
        ],
        isTop: [{ required: true, message: "置顶不能为空", trigger: "blur" }],
        status: [{ required: true, message: "状态不能为空", trigger: "blur" }],
        views: [{ required: true, message: "浏览量不能为空", trigger: "blur" }],
        collects: [
          { required: true, message: "收藏量不能为空", trigger: "blur" },
        ],
        favorites: [
          { required: true, message: "点赞量不能为空", trigger: "blur" },
        ],
        delFlag: [
          { required: true, message: "删除标志不能为空", trigger: "blur" },
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" },
        ],
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" },
        ],
        deptId: [
          { required: true, message: "部门id不能为空", trigger: "blur" },
        ],
      },
      shenhe: false,
      aastatused: "",
      treeSelectProps: {
        value: "id", // ID字段名
        label: "typeName", // 显示名称
        // children: 'children'    // 子级字段名
      },
      treeSelectOptions: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询供需产品信息列表 */
    getList() {
      this.loading = true;
      listInfo(this.queryParams).then((response) => {
        this.infoList = response.rows;
        this.infoList.forEach((element) => {
          if (element.cover !== null) {
            element.cover = element.cover.split(",");
          }
        });
        this.total = response.total;
        this.loading = false;
      });
    },
    handleimg(eve) {
      this.handlemaimg = true;
      this.handleSrc = eve.handleQrUrl;
    },
    // 预览
    handlesee(row) {
      this.reset();
      const id = row.id || this.ids;
      getInfo(id).then((response) => {
        this.form = response.data;
        if (this.form.cover !== null && this.form.cover !== "") {
          this.imgs = this.form.cover.split(",");
        } else {
          this.imgs = [];
        }
        if (this.form.content !== null) {
          this.form.content = this.form.content.replace(
            /<img/g,
            "<img style='max-width:90%;height:auto;'"
          );
        }
        this.opensee = true;
        this.title = "预览";
      });
    },
    linkTo() {
      let activeIndex = this.$refs.carousel.activeIndex;
      this.$router.push(this.imgs[activeIndex].link);
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.shenhe = false;
      this.form = {
        id: null,
        name: null,
        enterpriseBelong: null,
        contactPerson: null,
        contactPhone: null,
        typeId: null,
        auth: null,
        platformPrice: null,
        marketPrice: null,
        handleCode: null,
        cover: null,
        videoUrl: null,
        introduce: null,
        content: null,
        recommend: null,
        isTop: null,
        remark: null,
        status: "0",
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
      };
      this.resetForm("form");
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      if (selection.length !== "0") {
        this.single = false;
      }
      this.multiple = !selection.length;
    },
    /** 驳回按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.open = true;
      console.log(row);
      this.title = "驳回原因";
      const id = row.id || this.ids;
      this.id = row.id;
    },
    // 批量通过/驳回
    resetparam(val) {
      let abb = "";
      if (val == "add0") {
        abb = "0";
      } else {
        abb = "1";
      }
      const add = {
        ids: this.ids,
        status: abb,
      };
      this.$modal
        .confirm("是否确认操作")
        .then(function () {
          return updetainfois(add);
        })
        .then(() => {
          this.$modal.msgSuccess("审核成功");
          this.getList();
          this.single = true;
        });
    },
    /** 通过按钮操作 */
    handletongguo(row) {
      const add = {
        status: "2",
        remark: this.remark,
        id: row.id,
      };
      this.$modal
        .confirm("是否确认通过")
        .then(function () {
          return updetareinfo(add);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("审核成功");
        })
        .catch(() => {});
    },
    /** 提交按钮 */
    submit() {
      const add = {
        status: "1",
        remark: this.remark,
        id: this.id,
      };

      updetareinfo(add).then(() => {
        this.$modal.msgSuccess("审核成功");
        this.open = false;
        this.remark = "";
        this.getList();
      });
    },

    /** 导出按钮操作 */
    // handleExport() {
    //   this.download(
    //     "supply/info/export",
    //     {
    //       ...this.queryParams,
    //     },
    //     `info_${new Date().getTime()}.xlsx`
    //   );
    // },
  },
};
</script>
<style lang="scss" scoped>
.tablePic {
  height: 48px;
  width: 72px;
}
.seename {
  font-size: 26px;
  color: #000;
}
.marketpriced {
  font-size: 24px;
  color: orange;
}
.bigbox {
  width: 100%;
  height: 350px;
  margin-top: 0px;
  // height: 500px;
  // position:absolute;
  // overflow: hidden;
  .images {
    width: 50%;
    display: inline-block;
    height: 100%;
  }
  .names {
    float: right;
    display: inline-block;
    width: 45%;
    margin-left: 5%;
    margin-top: 0%;
    height: 300px;
  }
}
.view-dialog ::v-deep .el-dialog__body {
  padding: 0;
}
.introduce {
  line-height: 40px;
  font-weight: bold;
  font-size: 20px;
  color: #000;
  border-bottom: 1px solid #666;
}
</style>
