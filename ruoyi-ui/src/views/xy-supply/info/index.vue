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
          v-hasPermi="['supply:info:add']"
          >新增</el-button
        >
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['supply:info:remove']"
        >删除</el-button>
      </el-col>-->
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
            v-if="scope.row.handleQrUrl"
          >
            {{ scope.row.handleCode }}
          </div>

          <div
            v-if="!scope.row.handleQrUrl"
            style="overflow: hidden; text-overflow: ellipsis; color: #515a6e"
          >
            {{ scope.row.handleCode }}
          </div>
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
            <video
              @click="handlePictureCardPreview(scope.row.videoUrl)"
              :src="scope.row.videoUrl"
              v-if="scope.row.videoUrl"
            ></video>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="分类"
        width="120"
        align="center"
        prop="typeName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="平台价(元)"
        width="120"
        align="center"
        prop="platformPrice"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="市场价(元)"
        width="120"
        align="center"
        prop="marketPrice"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="所属企业"
        align="center"
        width="120"
        prop="enterpriseBelong"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        width="120"
        label="联系人"
        align="center"
        prop="contactPerson"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        width="120"
        label="联系方式"
        align="center"
        prop="contactPhone"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        width="120"
        label="作者"
        align="center"
        prop="auth"
        :show-overflow-tooltip="true"
      />
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
      <el-table-column
        width="120"
        label="置顶"
        align="center"
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
        width="120"
        label="状态"
        align="center"
        prop="statusValue"
        :show-overflow-tooltip="true"
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
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-if="
              scope.row.status == '0' ||
              scope.row.status == '1' ||
              scope.row.status == '5'
            "
            v-hasPermi="['supply:info:edit']"
            >修改</el-button
          >
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
            v-hasPermi="['supply:info:remove']"
            >删除</el-button
          >
          <el-button
            v-if="scope.row.status == '3'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handlefabu(scope.row)"
            v-hasPermi="['supply:info:release']"
            >发布</el-button
          >
          <el-button
            v-if="scope.row.status == '4'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handletuihui(scope.row)"
            v-hasPermi="['supply:info:cancel']"
            >取消发布</el-button
          >
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
            v-hasPermi="['supply:info:submit']"
            >提交审核</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleReject(scope.row)"
            v-if="scope.row.status == '2' || scope.row.status == '3'"
            v-hasPermi="['supply:info:withdraw']"
            >撤回</el-button
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
      width="1000px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="产品名称" prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入产品名称"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="所属企业" prop="enterpriseBelongId">
          <!-- <el-input
            v-model="form.enterpriseBelong"
            placeholder="请输入所属企业"
          />-->
          <el-select v-model="form.enterpriseBelongId" placeholder="请选择">
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
              <el-input
                v-model="form.contactPhone"
                placeholder="请输入联系方式"
              />
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
              <el-input v-model="form.auth" 
              maxlength="50"
                show-word-limit
              placeholder="请输入作者" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="平台价" prop="platformPrice">
              <el-input-number
                v-model="form.platformPrice"
                controls-position="right"
                :min="0"
                precision="2"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="市场价" prop="marketPrice">
              <el-input-number
                v-model="form.marketPrice"
                controls-position="right"
                :min="0"
                precision="2"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="18">
            <el-form-item label="Handle码" prop="handleCode">
              <el-input
                v-model="form.handleCode"
                placeholder="请输入Handle码"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item>
              <el-button type="primary" @click="erweima(form.handleCode)"
                >生成二维码</el-button
              >
            </el-form-item>
            <el-form-item v-if="form.handleQrUrl">
              <el-image
                style="width: 150px; height: 150px"
                :src="form.handleQrUrl"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="产品图片" prop="cover">
          <upload-imgs
            :images="form.cover"
            @upload="uploadSuccess"
          ></upload-imgs>
        </el-form-item>

        <el-form-item label="产品视频" prop="videoUrl" label-width="120px">
          <UploadVideo :videos="videos" @change="getVideo"></UploadVideo>
        </el-form-item>
        <el-form-item label="产品简介" prop="introduce">
          <el-input
            v-model="form.introduce"
            type="textarea"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="产品内容" prop="content">
          <editor class="ql-editor" v-model="form.content" :min-height="192" />
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
        <el-form-item label="产品备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :disabled="shenhe" @click="submitForm('add')"
          >提交审核</el-button
        >
        <el-button type="primary" @click="submitForm('att')"
          >保存草稿</el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="opensee"
      class="view-dialog"
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
              <div class="price">
                <span class="platform-price"
                  >￥{{ form.platformPrice ? form.platformPrice : "--" }}</span
                >
                <span class="market-price"
                  >￥{{ form.marketPrice ? form.marketPrice : "--" }}</span
                >
                <span class="discount">{{
                  form.platformPrice && form.marketPrice
                    ? ((form.platformPrice / form.marketPrice) * 10).toFixed(
                        1
                      ) + "折"
                    : "--"
                }}</span>
              </div>
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
      :visible.sync="dialogVisible"
      append-to-body
      center
      title="视频查看"
    >
      <video :src="videoSrc" ref="video" controls alt width="100%">
        您的浏览器不支持视频播放
      </video>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :visible.sync="handlemaimg"
      append-to-body
      width="500px"
      center
      title="二维码查看"
    >
      <img :src="handleSrc" controls alt width="100%" />
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
            <el-input
              v-model="remark"
              type="textarea"
              :placeholder="'请输入' + reason"
            />
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
  listInfo,
  getInfo,
  delInfo,
  addInfo,
  updateInfo,
  fabuInfo,
  fenleiInfo,
  erweiiInfo,
  uploadInfo,
  submit,
  withdraw,
  getEnterpriseList,
  recommendTop,
} from "@/api/xy-supply/info";
import TreeSelecter from "../../../components/treeSelect/index.vue";
import upLoadPic from "../../../components/upLoadPic";
import UploadVideo from "../../../components/uploadVideo";
import UploadImgs from "@/components/UploadImgs";
import { validPhone } from "@/utils/wdcspValidate.js";
export default {
  name: "Info",
  components: { TreeSelecter, upLoadPic, UploadVideo, UploadImgs },
  data() {
    var checkContactPhone = (rule, value, callback) => {
      if (!value) {
        return callback();
      }
      setTimeout(() => {
        if (!validPhone(value)) {
          callback(new Error("请输入电话号或手机号"));
        } else {
          callback();
        }
      }, 300);
    };
    let isTrueUrl = (rule, value, callback) => {
      let reg = /^8[6]/;
      if (!reg.test(value)) {
        callback(new Error("请以86开头"));
      } else {
        callback();
      }
    };
    return {
      enterpriseOptions: [],
      covers: "",
      handlemaimg: false,
      videoSrc: "",
      handleSrc: "",
      morentup: [require("@/assets/images/profile.jpg")],
      dialogImageUrl: "",
      dialogVisible: false,
      disabled: false,
      videos: [],
      //控制图片预览窗口的显示与隐藏
      previewVisible: false,
      addForm: [],

      imgs: [],
      //图片地址
      previewPath: "",
      videoInfo: {
        allVideo: [],
        deleteVideo: [],
      },

      cover1: "",
      // 二维码
      handleerwei: "",
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
        enterpriseBelongId: [
          { required: true, message: "所属企业不能为空", trigger: "change" },
        ],
        contactPerson: [
          { required: true, message: "联系人不能为空", trigger: "blur" },
        ],
        contactPhone: [
          { required: true, message: "联系方式不能为空", trigger: "blur" },
          { validator: checkContactPhone, trigger: "blur" },
        ],
        name: [
          { required: true, message: "产品名称不能为空", trigger: "blur" },
        ],
        typeId: [
          { required: true, message: "分类id不能为空", trigger: "blur" },
        ],
        handleCode: [
          { required: true, message: "Handle码不能为空", trigger: "blur" },
          { validator: isTrueUrl, trigger: "blur" },
        ],
        content: [
          { required: true, message: "产品内容不能为空", trigger: "blur" },
        ],
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
      currentId: "", // 操作行id
      remark: "", // 撤回，取消发布备注
      visible: false, // 撤回，取消发布弹窗
      rejectType: null, // 撤回or取消发布 0:取消发布 , 1:撤回
      reason: "", // 撤回or取消发布 text
    };
  },
  watch: {
    "form.content"(val) {
      let reg = /^(\<p\>)\s+(\<\/p\>)$/g;
      if (val == "<p><br></p>" || reg.test(val)) {
        this.form.content = "";
      }
    },
  },
  created() {
    this.getList();
    this.fenlei();
    this.loadEnterpriseList();
  },
  methods: {
    // ? 查询所有企业分类
    async loadEnterpriseList() {
      const res = await getEnterpriseList({});
      console.log("res:企业详情 ", res);
      this.enterpriseOptions = res.data;
    },
    /** 查询供需产品信息列表 */
    uploadFile: (file) => {},
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
    uploadSuccess(val) {
      this.form.cover = val.join(",");
    },

    // 上传到暂存区
    getVideo(event) {
      if (event && event.length > 0) {
        this.videos = event;
        this.form.videoUrl = event.map((ele) => ele.url).join(",");
      } else {
        this.videos = [];
        this.form.videoUrl = "";
      }
    },
    // 二维码请求
    erweima(val) {
      const add = {
        handleCode: val,
        type: "1",
      };
      if (val !== null) {
        erweiiInfo(add).then((res) => {
          this.form.handleQrUrl = res.msg;
        });
      } else if (val == null) {
        this.$modal.confirm("请填写handle码");
      }
    },
    // 新增产品分类
    fenlei() {
      fenleiInfo(this.queryParams).then((res) => {
        this.treeSelectOptions = res.data;
        console.log(res, "000000000000000");
      });
    },
    // 发布
    handlefabu(row) {
      this.currentId = row.id;
      this.aastatused = "2";
      this.submit();
    },
    // 发布撤回
    handletuihui(row) {
      this.currentId = row.id;
      this.aastatused = "3";
      this.rejectType = 0;
      this.visible = true;
      this.title = "取消发布";
      this.reason = "取消原因";
    },
    // 撤回
    handleReject(row) {
      this.currentId = row.id;
      this.rejectType = 1;
      this.visible = true;
      this.title = "撤回";
      this.reason = "撤回原因";
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
    switchChange1(n) {
      const add = {
        status: 0,
        id: n.id,
        recommend: n.recommend,
      };
      recommendTop(add).then((res) => {
        this.$modal.msgSuccess("修改成功");
        this.getList();
      });
    },
    switchChange2(n) {
      const att = {
        status: 1,
        id: n.id,
        isTop: n.isTop,
      };
      recommendTop(att).then((response) => {
        this.$modal.msgSuccess("修改成功");
        this.getList();
      });
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
        handleQrUrl: null,
        platformPrice: null,
        marketPrice: null,
        handleCode: null,
        cover: null,
        videoUrl: null,
        introduce: null,
        content: null,
        recommend: "0",
        isTop: "0",
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

    handlePictureCardPreview(res) {
      this.dialogVisible = true;
      this.videoSrc = res;
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
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.videos = [];
      this.title = "添加供需产品信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();

      const id = row.id || this.ids;
      getInfo(id).then((response) => {
        this.form = response.data;
        console.log("this.form: 哈哈哈", this.form);
        let obj = {},
          arr = [];
        if (this.form.videoUrl) {
          this.form.videoUrl.split(",").forEach((ele) => {
            obj.url = ele;
            arr.push(obj);
          });
        }
        this.videos = arr;
        this.open = true;
        if (row.status == "0" || row.status == "1" || row.status == "5") {
          this.shenhe = false;
        } else {
          this.shenhe = true;
        }
        this.title = "修改供需产品信息";
      });
    },
    // 发布提交
    submit() {
      const add = {
        id: this.currentId,
        status: this.aastatused,
        remark: this.rejectType == 0 ? this.remark : null,
      };
      fabuInfo(add).then((response) => {
        if (add.status == "2") {
          this.$modal.msgSuccess("发布成功");
        } else {
          this.$modal.msgSuccess("取消发布成功");
        }
        this.getList();
      });
    },
    // 撤回提交
    submitReject() {
      switch (this.rejectType) {
        case 0:
          this.submit();
          break;
        case 1:
          withdraw({
            id: this.currentId,
            remark: this.remark,
          }).then(() => {
            this.$modal.msgSuccess("撤回成功");
            this.getList();
          });
          break;
      }
      this.cancelReject();
    },
    cancelReject() {
      this.visible = false;
      this.reason = "";
      this.remark = "";
      this.rejectType = null;
      this.currentId = "";
    },
    // 提交审核按钮操作
    submitFaob(row) {
      const add = {
        id: row.id,
      };
      this.$modal
        .confirm("是否确认操作？")
        .then(function () {
          return submit(add);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("提交成功");
        });
    },
    /** 提交按钮 */
    submitForm(val) {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            if (val == "add") {
              this.form.addStatus = "0";
            }
            if (val == "att") {
              this.form.addStatus = "";
            }
            const att = this.form;
            updateInfo(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            if (val == "add") {
              this.form.addStatus = "2";
            }
            if (val == "att") {
              this.form.addStatus = "0";
            }
            const add = this.form;
            addInfo(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm("是否确认删除所选产品？")
        .then(function () {
          return delInfo(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
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
.price {
  display: flex;
  align-items: center;
  .platform-price {
    font-size: 36px;
    color: orange;
  }
  .market-price {
    font-size: 16px;
    margin-left: 30px;
    margin-right: 8px;
    margin-top: 10px;
    text-decoration: line-through;
  }
  .discount {
    padding: 1px 3px;
    color: #f59a23;
    background-color: rgba(250, 205, 145, 0.6);
  }
}
.bigbox {
  width: 100%;
  min-height: 350px;
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
