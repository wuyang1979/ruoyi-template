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
      <el-form-item label="参数名称" prop="configName">
        <el-input
          v-model="queryParams.configName"
          placeholder="请输入参数名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="configList">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="参数主键" align="center" prop="configId" />
      <el-table-column label="参数名称" align="center" prop="configName" :show-overflow-tooltip="true" />
      <el-table-column label="参数键名" align="center" prop="configKey" :show-overflow-tooltip="true" />
      <el-table-column
        label="参数键值"
        align="center"
        prop="configValue"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="系统内置" align="center" prop="configType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.configType" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:config:edit']"
          >修改</el-button>
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

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open1" width="500px" destroy-on-close append-to-body>
      <el-form ref="form1" :model="form1" :rules="rules1" label-width="120px">
        <el-form-item label="参数名称" prop="configName">
          <el-input v-model="form1.configName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数键名" prop="configKey">
          <el-input disabled v-model="form1.configKey" placeholder="请输入参数键名" />
        </el-form-item>
        <el-form-item label="系统内置" prop="configType">
          <el-radio-group v-model="form1.configType">
            <el-radio
              v-for="dict in dict.type.sys_yes_no"
              disabled
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="账号密码登录" prop="password">
          <el-select v-model="form1.password" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="手机验证码登录" prop="verifyCode">
          <el-select v-model="form1.verifyCode" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form1.remark"
            :rows="2"
            type="textarea"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm1">确 定</el-button>
        <el-button @click="cancel1">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open2" width="500px" destroy-on-close append-to-body>
      <el-form ref="form2" :model="form2" :rules="rules2" label-width="120px">
        <el-form-item label="参数名称" prop="configName">
          <el-input v-model="form2.configName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数键名" prop="configKey">
          <el-input disabled v-model="form2.configKey" placeholder="请输入参数键名" />
        </el-form-item>
        <el-form-item label="系统内置" prop="configType">
          <el-radio-group v-model="form2.configType">
            <el-radio
              v-for="dict in dict.type.sys_yes_no"
              disabled
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否提示" prop="isMaintenance">
          <el-select v-model="form2.isMaintenance" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="提示内容" prop="content">
          <el-input v-model="form2.content" :rows="2" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form2.remark"
            :rows="2"
            type="textarea"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm2">确 定</el-button>
        <el-button @click="cancel2">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open3" width="500px" destroy-on-close append-to-body>
      <el-form ref="form3" :model="form3" :rules="rules3" label-width="120px">
        <el-form-item label="参数名称" prop="configName">
          <el-input v-model="form3.configName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数键名" prop="configKey">
          <el-input disabled v-model="form3.configKey" placeholder="请输入参数键名" />
        </el-form-item>
        <el-form-item label="系统内置" prop="configType">
          <el-radio-group v-model="form3.configType">
            <el-radio
              v-for="dict in dict.type.sys_yes_no"
              disabled
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="appId" prop="appId">
          <el-input v-model="form3.appId" placeholder="请输入appId" />
        </el-form-item>
        <el-form-item label="secretId" prop="secretId">
          <el-input v-model="form3.secretId" placeholder="请输入secretId" />
        </el-form-item>
        <el-form-item label="secretKey" prop="secretKey">
          <el-input v-model="form3.secretKey" placeholder="请输入secretKey" />
        </el-form-item>
        <el-form-item label="smsSdkAppId" prop="smsSdkAppId">
          <el-input v-model="form3.smsSdkAppId" placeholder="请输入smsSdkAppId" />
        </el-form-item>
        <el-form-item label="smsSign" prop="smsSign">
          <el-input v-model="form3.smsSign" placeholder="请输入smsSign" />
        </el-form-item>
        <el-form-item label="templateId" prop="templateId">
          <el-input v-model="form3.templateId" placeholder="请输入templateId" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form3.remark"
            :rows="2"
            type="textarea"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm3">确 定</el-button>
        <el-button @click="cancel3">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open4" width="500px" destroy-on-close append-to-body>
      <el-form ref="form4" :model="form4" :rules="rules4" label-width="120px">
        <el-form-item label="参数名称" prop="configName">
          <el-input v-model="form4.configName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数键名" prop="configKey">
          <el-input disabled v-model="form4.configKey" placeholder="请输入参数键名" />
        </el-form-item>
        <el-form-item label="系统内置" prop="configType">
          <el-radio-group v-model="form4.configType">
            <el-radio
              v-for="dict in dict.type.sys_yes_no"
              disabled
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="版权" prop="copyright">
          <el-input v-model="form4.copyright" placeholder="请输入copyright版权" />
        </el-form-item>
        <el-form-item label="备案号" prop="ICP">
          <el-input v-model="form4.ICP" placeholder="请输入ICP备案号" />
        </el-form-item>
        <el-form-item label="公安备案号" prop="police">
          <el-input v-model="form4.police" placeholder="请输入公安备案号" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form4.remark"
            :rows="2"
            type="textarea"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm4">确 定</el-button>
        <el-button @click="cancel4">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open5" width="500px" destroy-on-close append-to-body>
      <el-form ref="form5" :model="form5" :rules="rules5" label-width="120px">
        <el-form-item label="参数名称" prop="configName">
          <el-input v-model="form5.configName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数键名" prop="configKey">
          <el-input disabled v-model="form5.configKey" placeholder="请输入参数键名" />
        </el-form-item>
        <el-form-item label="系统内置" prop="configType">
          <el-radio-group v-model="form5.configType">
            <el-radio
              v-for="dict in dict.type.sys_yes_no"
              disabled
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="appId" prop="appId">
          <el-input v-model="form5.appId" placeholder="请输入appId" />
        </el-form-item>
        <el-form-item label="secret" prop="secret">
          <el-input v-model="form5.secret" placeholder="请输入secret" />
        </el-form-item>
        <el-form-item label="aesKey" prop="aesKey">
          <el-input v-model="form5.aesKey" placeholder="请输入aesKey" />
        </el-form-item>
        <el-form-item label="token" prop="token">
          <el-input v-model="form5.token" placeholder="请输入token" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form5.remark"
            :rows="2"
            type="textarea"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm5">确 定</el-button>
        <el-button @click="cancel5">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open6" width="500px" destroy-on-close append-to-body>
      <el-form ref="form6" :model="form6" :rules="rules6" label-width="120px">
        <el-form-item label="参数名称" prop="configName">
          <el-input v-model="form6.configName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数键名" prop="configKey">
          <el-input disabled v-model="form6.configKey" placeholder="请输入参数键名" />
        </el-form-item>
        <el-form-item label="系统内置" prop="configType">
          <el-radio-group v-model="form6.configType">
            <el-radio
              v-for="dict in dict.type.sys_yes_no"
              disabled
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form6.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="电话" prop="TEL">
          <el-input v-model="form6.TEL" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form6.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="技术支持" prop="techSupport">
          <el-input v-model="form6.techSupport" placeholder="请输入技术支持" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form6.remark"
            :rows="2"
            type="textarea"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm6">确 定</el-button>
        <el-button @click="cancel6">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open7" width="1200px" destroy-on-close append-to-body>
      <el-form ref="form7" :model="form7" :rules="rules7" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="参数名称" prop="configName">
              <el-input v-model="form7.configName" placeholder="请输入参数名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参数键名" prop="configKey">
              <el-input disabled v-model="form7.configKey" placeholder="请输入参数键名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="系统内置" prop="configType">
          <el-radio-group v-model="form7.configType">
            <el-radio
              v-for="dict in dict.type.sys_yes_no"
              disabled
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-row :gutter="10" v-for="(item,index) in form7.array" :key="index">
          <el-col :span="needCenter?4:5">
            <el-form-item
              label="名称"
              :prop="'array.' + index + '.name'"
              :rules="{
                required: true, message: '名称不能为空', trigger: 'blur'
              }"
            >
              <el-input v-model="item.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="needCenter?4:5">
            <el-form-item
              label="数量"
              :prop="'array.' + index + '.number'"
              :rules="{
                required: true, message: '数量不能为空', trigger: 'blur'
              }"
            >
              <el-input-number
                style="width: 100%"
                v-model="item.number"
                placeholder="请输入数量"
                step-strictly
                :controls="false"
                :min="1"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="needCenter?4:5">
            <el-form-item
              label="经度"
              :prop="'array.' + index + '.longitude'"
              :rules="{
                required: true, message: '经度不能为空', trigger: 'blur'
              }"
            >
              <el-input-number
                style="width: 100%"
                v-model="item.longitude"
                placeholder="请输入经度"
                :controls="false"
              ></el-input-number>
              <!-- :min="-180"
              :max="180"-->
            </el-form-item>
          </el-col>
          <el-col :span="needCenter?4:5">
            <el-form-item
              label="纬度"
              :prop="'array.' + index + '.latitude'"
              :rules="{
                required: true, message: '纬度不能为空', trigger: 'blur'
              }"
            >
              <el-input-number
                style="width: 100%"
                v-model="item.latitude"
                placeholder="请输入纬度"
                :controls="false"
              ></el-input-number>
              <!-- :min="-90"
              :max="90"-->
            </el-form-item>
          </el-col>
          <el-col :span="needCenter?4:5" v-if="needCenter">
            <el-form-item
              label="中心点"
              :prop="'array.' + index + '.center'"
              :rules="{
                required: true, message: '请选择是否中心点', trigger: 'change'
              }"
            >
              <el-select
                v-model="item.center"
                @change="(val) => changeCenter(val,index)"
                placeholder="请选择"
              >
                <el-option label="是" :value="1"></el-option>
                <el-option label="否" :value="0"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="2" style="text-align: right">
            <el-button type="primary" @click.prevent="addItem(index)">添加</el-button>
          </el-col>
          <el-col :span="2">
            <el-button type="danger" @click.prevent="removeItem(index)">删除</el-button>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form7.remark"
            :rows="2"
            type="textarea"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm7">确 定</el-button>
        <el-button @click="cancel7">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listSysConfig,
  getSysDetail,
  editSysConfig,
} from '@/api/system/sysconfig'

export default {
  name: 'Config',
  dicts: ['sys_yes_no'],
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
      // 参数表格数据
      configList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open1: false,
      open2: false,
      open3: false,
      open4: false,
      open5: false,
      open6: false,
      open7: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        configName: undefined,
        configKey: undefined,
        configType: undefined,
      },
      options: [
        {
          value: true,
          label: '是',
        },
        {
          value: false,
          label: '否',
        },
      ],
      // 表单参数
      form1: {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        password: true,
        verifyCode: true,
        remark: undefined,
      },
      form2: {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        isMaintenance: true,
        content: '',
        remark: undefined,
      },
      form3: {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        remark: undefined,
        appId: '',
        secretId: '',
        secretKey: '',
        smsSdkAppId: '',
        smsSign: '',
        templateId: '',
      },
      form4: {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        remark: undefined,
        copyright: '',
        ICP: '',
        police: '',
      },
      form5: {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        remark: undefined,
        appId: '',
        secret: '',
        aesKey: '',
        token: '',
      },
      form6: {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        remark: undefined,
        address: '',
        TEL: '',
        email: '',
        techSupport: '',
      },
      needCenter: true,
      form7: {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        remark: undefined,
        array: [],
      },
      // 表单校验
      rules1: {
        configName: [
          { required: true, message: '参数名称不能为空', trigger: 'blur' },
        ],
      },
      rules2: {
        configName: [
          { required: true, message: '参数名称不能为空', trigger: 'blur' },
        ],
        content: [
          { required: true, message: '提示内容不能为空', trigger: 'blur' },
        ],
      },
      rules3: {
        configName: [
          { required: true, message: '参数名称不能为空', trigger: 'blur' },
        ],
        appId: [{ required: true, message: 'appId不能为空', trigger: 'blur' }],
        secretId: [
          { required: true, message: 'secretId不能为空', trigger: 'blur' },
        ],
        secretKey: [
          { required: true, message: 'secretKey不能为空', trigger: 'blur' },
        ],
        smsSdkAppId: [
          { required: true, message: 'smsSdkAppId不能为空', trigger: 'blur' },
        ],
        smsSign: [
          { required: true, message: 'smsSign不能为空', trigger: 'blur' },
        ],
        templateId: [
          { required: true, message: 'templateId不能为空', trigger: 'blur' },
        ],
      },
      rules4: {
        configName: [
          { required: true, message: '参数名称不能为空', trigger: 'blur' },
        ],
        copyright: [
          { required: true, message: 'copyright不能为空', trigger: 'blur' },
        ],
        ICP: [{ required: true, message: 'ICP不能为空', trigger: 'blur' }],
        police: [
          { required: true, message: 'police不能为空', trigger: 'blur' },
        ],
      },
      rules5: {
        configName: [
          { required: true, message: '参数名称不能为空', trigger: 'blur' },
        ],
        appId: [{ required: true, message: 'appId不能为空', trigger: 'blur' }],
        secret: [
          { required: true, message: 'secret不能为空', trigger: 'blur' },
        ],
      },
      rules6: {
        configName: [
          { required: true, message: '参数名称不能为空', trigger: 'blur' },
        ],
        address: [{ required: true, message: '地址不能为空', trigger: 'blur' }],
        TEL: [{ required: true, message: '电话不能为空', trigger: 'blur' }],
        email: [{ required: true, message: '邮箱不能为空', trigger: 'blur' }],
        techSupport: [
          { required: true, message: '技术支持不能为空', trigger: 'blur' },
        ],
      },
      rules7: {
        configName: [
          { required: true, message: '参数名称不能为空', trigger: 'blur' },
        ],
        address: [{ required: true, message: '地址不能为空', trigger: 'blur' }],
        TEL: [{ required: true, message: '电话不能为空', trigger: 'blur' }],
        email: [{ required: true, message: '邮箱不能为空', trigger: 'blur' }],
        techSupport: [
          { required: true, message: '技术支持不能为空', trigger: 'blur' },
        ],
      },
    }
  },
  created() {
    this.getList()
    // this.getConfigKey();
  },
  methods: {
    /** 查询参数列表 */
    getList() {
      this.loading = true
      listSysConfig(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.configList = response.rows
          this.total = response.total
          this.loading = false
        }
      )
    },
    // getConfigKey() {
    //   this.loading = true;
    //   getConfigKey('loginType').then(response => {
    //       console.log(response,'configKey')
    //     }
    //   );
    // },
    // 取消按钮
    cancel1() {
      this.open1 = false
      this.reset1()
    },
    cancel2() {
      this.open2 = false
      this.reset2()
    },
    cancel3() {
      this.open3 = false
      this.reset3()
    },
    cancel4() {
      this.open4 = false
      this.reset4()
    },
    cancel5() {
      this.open5 = false
      this.reset5()
    },
    cancel6() {
      this.open6 = false
      this.reset6()
    },
    cancel7() {
      this.open7 = false
      this.reset7()
    },
    // 表单重置
    reset1() {
      this.form1 = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        password: true,
        verifyCode: true,
        remark: undefined,
      }
      this.resetForm('form1')
    },
    reset2() {
      this.form2 = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        isMaintenance: true,
        content: '',
        remark: undefined,
      }
      this.resetForm('form2')
    },
    reset3() {
      this.form3 = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        appId: '',
        secretId: '',
        secretKey: '',
        smsSdkAppId: '',
        smsSign: '',
        templateId: '',
        remark: undefined,
      }
      this.resetForm('form3')
    },
    reset4() {
      this.form4 = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        copyright: '',
        ICP: '',
        police: '',
        remark: undefined,
      }
      this.resetForm('form4')
    },
    reset5() {
      this.form5 = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        appId: '',
        secret: '',
        aesKey: '',
        token: '',
        remark: undefined,
      }
      this.resetForm('form5')
    },
    reset6() {
      this.form6 = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        address: '',
        TEL: '',
        email: '',
        techSupport: '',
        remark: undefined,
      }
      this.resetForm('form6')
    },
    reset7() {
      this.form7 = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'N',
        array: [],
        remark: undefined,
      }
      this.resetForm('form7')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const configId = row.configId || this.ids

      getSysDetail(configId).then((res) => {
        if (res.data.configKey === 'xyLoginType') {
          this.reset1()
          this.form1.configId = res.data.configId
          this.form1.configValue = res.data.configValue
          this.form1.configKey = res.data.configKey
          this.form1.configName = res.data.configName
          this.form1.configType = res.data.configType
          this.form1.password = res.data.array[0].password
          this.form1.verifyCode = res.data.array[0].verifyCode
          this.form1.remark = res.data.remark
          this.open1 = true
          this.title = '修改参数'
        } else if (res.data.configKey === 'xyMaintenanceNotice') {
          this.reset2()
          this.form2.configId = res.data.configId
          this.form2.configValue = res.data.configValue
          this.form2.configKey = res.data.configKey
          this.form2.configName = res.data.configName
          this.form2.configType = res.data.configType
          this.form2.content = res.data.array[0].content
          this.form2.isMaintenance = res.data.array[0].isMaintenance
          this.form2.remark = res.data.remark
          this.open2 = true
          this.title = '修改参数'
        } else if (res.data.configKey === 'xyMmsConfigure') {
          this.reset3()
          this.form3.configId = res.data.configId
          this.form3.configValue = res.data.configValue
          this.form3.configKey = res.data.configKey
          this.form3.configName = res.data.configName
          this.form3.configType = res.data.configType

          this.form3.appId = res.data.array[0].appId
          this.form3.secretId = res.data.array[0].secretId
          this.form3.secretKey = res.data.array[0].secretKey
          this.form3.smsSdkAppId = res.data.array[0].smsSdkAppId
          this.form3.smsSign = res.data.array[0].smsSign
          this.form3.templateId = res.data.array[0].templateId

          this.form3.remark = res.data.remark
          this.open3 = true
          this.title = '修改参数'
        } else if (
          res.data.configKey === 'xyWebConfigure' ||
          res.data.configKey === 'xyFrontCopyright'
        ) {
          this.reset4()
          this.form4.configId = res.data.configId
          this.form4.configValue = res.data.configValue
          this.form4.configKey = res.data.configKey
          this.form4.configName = res.data.configName
          this.form4.configType = res.data.configType

          this.form4.copyright = res.data.array[0].copyright
          this.form4.ICP = res.data.array[0].ICP
          this.form4.police = res.data.array[0].police
          this.form4.remark = res.data.remark
          this.open4 = true
          this.title = '修改参数'
        } else if (res.data.configKey === 'xyWxConfigure') {
          this.reset5()
          this.form5.configId = res.data.configId
          this.form5.configValue = res.data.configValue
          this.form5.configKey = res.data.configKey
          this.form5.configName = res.data.configName
          this.form5.configType = res.data.configType

          this.form5.appId = res.data.array[0].appId
          this.form5.secret = res.data.array[0].secret
          this.form5.aesKey = res.data.array[0].aesKey
          this.form5.token = res.data.array[0].token
          this.form5.remark = res.data.remark
          this.open5 = true
          this.title = '修改参数'
        } else if (res.data.configKey === 'xyFrontEnterprise') {
          this.reset6()
          this.form6.configId = res.data.configId
          this.form6.configValue = res.data.configValue
          this.form6.configKey = res.data.configKey
          this.form6.configName = res.data.configName
          this.form6.configType = res.data.configType

          this.form6.address = res.data.array[0].address
          this.form6.TEL = res.data.array[0].TEL
          this.form6.email = res.data.array[0].email
          this.form6.techSupport = res.data.array[0].techSupport
          this.form6.remark = res.data.remark
          this.open6 = true
          this.title = '修改参数'
        } else if (
          res.data.configKey === 'xyRootConfig' ||
          res.data.configKey === 'xyCountryConfig' ||
          res.data.configKey === 'xyWuxiConfig'
        ) {
          if (res.data.configKey === 'xyCountryConfig') {
            this.needCenter = false
          } else {
            this.needCenter = true
          }
          this.reset7()
          this.form7.configId = res.data.configId
          this.form7.configValue = res.data.configValue
          this.form7.configKey = res.data.configKey
          this.form7.configName = res.data.configName
          this.form7.configType = res.data.configType

          this.form7.array = res.data.array
          this.form7.remark = res.data.remark
          this.open7 = true
          this.title = '修改参数'
        }
      })
    },
    /** 提交按钮 */
    submitForm1: function () {
      this.$refs['form1'].validate((valid) => {
        if (valid) {
          if (this.form1.configId != undefined) {
            const params = {
              configId: this.form1.configId,
              configName: this.form1.configName,
              configKey: this.form1.configKey,
              configValue: this.form1.configValue,
              configType: this.form1.configType,
              array: [
                {
                  password: this.form1.password,
                  verifyCode: this.form1.verifyCode,
                },
              ],
              remark: this.form1.remark,
            }
            editSysConfig(params).then((response) => {
              this.$modal.msgSuccess('修改成功')
              this.open1 = false
              this.getList()
            })
          } else {
            addConfig(this.form1).then((response) => {
              this.$modal.msgSuccess('新增成功')
              this.open1 = false
              this.getList()
            })
          }
        }
      })
    },
    submitForm2: function () {
      this.$refs['form2'].validate((valid) => {
        if (valid) {
          if (this.form2.configId != undefined) {
            const params = {
              configId: this.form2.configId,
              configName: this.form2.configName,
              configKey: this.form2.configKey,
              configValue: this.form2.configValue,
              configType: this.form2.configType,
              array: [
                {
                  isMaintenance: this.form2.isMaintenance,
                  content: this.form2.content,
                },
              ],
              remark: this.form2.remark,
            }
            editSysConfig(params).then((response) => {
              this.$modal.msgSuccess('修改成功')
              this.open2 = false
              this.getList()
            })
          } else {
            addConfig(this.form2).then((response) => {
              this.$modal.msgSuccess('新增成功')
              this.open2 = false
              this.getList()
            })
          }
        }
      })
    },
    submitForm3: function () {
      this.$refs['form3'].validate((valid) => {
        if (valid) {
          if (this.form3.configId != undefined) {
            const params = {
              configId: this.form3.configId,
              configName: this.form3.configName,
              configKey: this.form3.configKey,
              configValue: this.form3.configValue,
              configType: this.form3.configType,
              array: [
                {
                  appId: this.form3.appId,
                  secretId: this.form3.secretId,
                  secretKey: this.form3.secretKey,
                  smsSdkAppId: this.form3.smsSdkAppId,
                  smsSign: this.form3.smsSign,
                  templateId: this.form3.templateId,
                },
              ],
              remark: this.form3.remark,
            }
            editSysConfig(params).then((response) => {
              this.$modal.msgSuccess('修改成功')
              this.open3 = false
              this.getList()
            })
          } else {
            addConfig(this.form3).then((response) => {
              this.$modal.msgSuccess('新增成功')
              this.open3 = false
              this.getList()
            })
          }
        }
      })
    },
    submitForm4: function () {
      this.$refs['form4'].validate((valid) => {
        if (valid) {
          if (this.form4.configId != undefined) {
            const params = {
              configId: this.form4.configId,
              configName: this.form4.configName,
              configKey: this.form4.configKey,
              configValue: this.form4.configValue,
              configType: this.form4.configType,
              array: [
                {
                  copyright: this.form4.copyright,
                  ICP: this.form4.ICP,
                  police: this.form4.police,
                },
              ],
              remark: this.form4.remark,
            }
            editSysConfig(params).then((response) => {
              this.$modal.msgSuccess('修改成功')
              this.open4 = false
              this.getList()
            })
          } else {
            addConfig(this.form4).then((response) => {
              this.$modal.msgSuccess('新增成功')
              this.open4 = false
              this.getList()
            })
          }
        }
      })
    },
    submitForm5: function () {
      this.$refs['form5'].validate((valid) => {
        if (valid) {
          if (this.form5.configId != undefined) {
            const params = {
              configId: this.form5.configId,
              configName: this.form5.configName,
              configKey: this.form5.configKey,
              configValue: this.form5.configValue,
              configType: this.form5.configType,
              array: [
                {
                  appId: this.form5.appId,
                  secret: this.form5.secret,
                  aesKey: this.form5.aesKey,
                  token: this.form5.token,
                },
              ],
              remark: this.form5.remark,
            }
            editSysConfig(params).then((response) => {
              this.$modal.msgSuccess('修改成功')
              this.open5 = false
              this.getList()
            })
          } else {
            addConfig(this.form5).then((response) => {
              this.$modal.msgSuccess('新增成功')
              this.open5 = false
              this.getList()
            })
          }
        }
      })
    },
    submitForm6: function () {
      this.$refs['form6'].validate((valid) => {
        if (valid) {
          if (this.form6.configId != undefined) {
            const params = {
              configId: this.form6.configId,
              configName: this.form6.configName,
              configKey: this.form6.configKey,
              configValue: this.form6.configValue,
              configType: this.form6.configType,
              array: [
                {
                  address: this.form6.address,
                  TEL: this.form6.TEL,
                  email: this.form6.email,
                  techSupport: this.form6.techSupport,
                },
              ],
              remark: this.form6.remark,
            }
            editSysConfig(params).then((response) => {
              this.$modal.msgSuccess('修改成功')
              this.open6 = false
              this.getList()
            })
          } else {
            addConfig(this.form6).then((response) => {
              this.$modal.msgSuccess('新增成功')
              this.open6 = false
              this.getList()
            })
          }
        }
      })
    },
    changeCenter(val, index) {
      if (val) {
        this.form7.array.forEach((ele, ind) => {
          if (index != ind) {
            ele.center = 0
          }
        })
      }
    },
    addItem(index) {
      console.log(index)
      this.form7.array.splice(index + 1, 0, {
        name: '',
        number: undefined,
        longitude: undefined,
        latitude: undefined,
        center: 0,
      })
    },
    removeItem(index) {
      this.form7.array.splice(index, 1)
    },
    submitForm7: function () {
      this.$refs['form7'].validate((valid) => {
        if (valid) {
          if (this.needCenter) {
            let data = this.form7.array.find((ele) => ele.center == 1)
            if (!data) {
              return this.$modal.msgError('请选择中心点')
            }
          }
          if (this.form7.configId != undefined) {
            const params = {
              configId: this.form7.configId,
              configName: this.form7.configName,
              configKey: this.form7.configKey,
              configValue: this.form7.configValue,
              configType: this.form7.configType,
              array: this.form7.array,
              remark: this.form7.remark,
            }
            editSysConfig(params).then((response) => {
              this.$modal.msgSuccess('修改成功')
              this.open7 = false
              this.getList()
            })
          } else {
            addConfig(this.form7).then((response) => {
              this.$modal.msgSuccess('新增成功')
              this.open7 = false
              this.getList()
            })
          }
        }
      })
    },
  },
}
</script>
