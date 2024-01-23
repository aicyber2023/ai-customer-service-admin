<template>
  <div class="review-type">
    <el-form
      :model="queryParams"
      ref="queryForm"
      class="dept_form"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="客户" prop="sysUserId" v-if="showSysUser">
        <el-select v-model="queryParams.sysUserId" clearable placeholder="请选择客户" size="small">
          <el-option
            v-for="item in userOptions"
            :key="item.userId"
            :label="item.nickName"
            :value="item.userId"
          />
        </el-select>
      </el-form-item>
      <!--      <el-form-item label="服务对象ID" prop="id">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.id"-->
      <!--          placeholder="请输入服务对象ID"-->
      <!--          clearable-->
      <!--          size="small"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <el-form-item label="用户名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入用户名"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="用户昵称" prop="nickName">
        <el-input
          v-model="queryParams.nickName"
          placeholder="请输入用户昵称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="当前状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择当前状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
        >重置
        </el-button
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
        >新增
        </el-button
        >
      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="success"-->
      <!--          plain-->
      <!--          icon="el-icon-edit"-->
      <!--          size="mini"-->
      <!--          :disabled="single"-->
      <!--          @click="handleUpdate"-->
      <!--        >修改-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDel"
        >删除
        </el-button>
      </el-col>
    </el-row>
    <el-table
      v-loading="loading"
      :data="tableData"
      default-expand-all
      @selection-change="handleSelectionChange"
      height="550px"
    >
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column prop="sysUserId" label="客户ID" align="center" width="120" v-if="checkRoles()==9">
        <template slot-scope="scope">
          {{ scope.row.sysUserId | filterUser(userOptions) }}
        </template>
      </el-table-column>
      <!--      <el-table-column prop="id" label="服务对象ID" align="center" width="100"></el-table-column>-->
      <el-table-column prop="name" label="用户名" align="center" min-width="100"></el-table-column>
      <el-table-column prop="nickName" label="用户昵称" align="center" min-width="150"></el-table-column>
      <el-table-column prop="face" label="用户头像" align="center" width="100">
        <template slot-scope="scope">
          <el-image style="height: 40px;margin-bottom: -8px;" :src="getUrl(scope.row.openId)" fit="fill">
            <div slot="error" class="image-slot" style="line-height: 40px">
              <!--              <el-image :src="require('@/assets/images/default.jpg')" fit="fill"></el-image>-->
              无
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="当前状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | filterCss(statusOptions)">
            {{ scope.row.status | filterStage(statusOptions) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center" width="200"></el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="250"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-video-pause"
            @click="handleChangeStatus(scope.row, 2)"
            v-if="scope.row.status == 1"
          >封号
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-video-play"
            @click="handleChangeStatus(scope.row, 1)"
            v-else
          >解封
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-money"
            @click="handleChangePassword(scope.row)"
          >重置密码
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDel(scope.row)"
          >删除
          </el-button
          >
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

    <!-- 添加或修改【请填写功能名称】对话框 -->
    <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="客户" prop="sysUserId" v-if="showSysUser">
          <el-select v-model="form.sysUserId" placeholder="请选择客户" size="small">
            <el-option
              v-for="item in userOptions"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="用户名" prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入用户名"
            clearable
            size="small"
          />
        </el-form-item>
        <el-form-item label="用户昵称" prop="nickName">
          <el-input
            v-model="form.nickName"
            placeholder="请输入用户昵称"
            clearable
            size="small"
          />
        </el-form-item>
        <el-form-item label="登录密码" prop="password" v-if="showPassword">
          <el-input
            v-model="form.password"
            placeholder="请输入登录密码"
            show-password
            clearable
            size="small"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status" size="small">
            <el-radio v-for="dict in statusOptions" :label="dict.dictValue">{{ dict.dictLabel }}</el-radio>
          </el-radio-group>
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
import {add, del, get, list, update, updatePwd} from '@/api/business/user'
import {listData} from "@/api/system/dict/data";
import {listAll} from "@/api/system/user";
import {getInfo} from '@/api/login'

export default {
  name: "index",
  data() {
    return {
      baseUrl:window.cfg.baseUrl,
      loading: false,
      tableData: [],
      statusOptions: [],
      userOptions: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 12,
        sysUserId: undefined,
        id: undefined,
        name: "",
        nickName: "",
        status: undefined
      },
      showSysUser: false,
      showSearch: true,
      showPassword: false,
      open: false,
      title: "追加服务对象",
      userInfo: {},
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      form: {},
      rules: {
        sysUserId: [{required: true, message: "请选择客户", trigger: "change"}],
        name: [{required: true, message: "请输入用户名", trigger: "blur"}],
        // nickName:[{ required: true, message: "请输入用户昵称", trigger: "blur" }],
        password: [{required: true, message: "请输入用户密码", trigger: "blur"}],
      }
    }
  },
  filters: {
    filterStage: function (val, list) {
      if (list && list.length != 0) {
        return list.filter(v => v.dictValue == val)[0].dictLabel;
      }
      return val;
    },
    filterCss: function (val, list) {
      if (list && list.length != 0) {
        return list.filter(v => v.dictValue == val)[0].listClass;
      }
      return val;
    },
    filterUser: function (val, list) {
      if (list && list.length != 0) {
        return list.filter(v => v.userId == val)[0].nickName;
      }
      return val;
    },
  },
  methods: {
    getList() {
      list(this.queryParams).then(res => {
        if (res.code == 200) {
          this.tableData = res.rows
          this.total = res.total
        }
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.sysUserId = undefined;
      if (!this.showSysUser) {
        this.queryParams.sysUserId = this.userInfo.userId
      }
      this.queryParams.id = undefined
      this.queryParams.nickName = ""
      this.queryParams.status = undefined
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 表单重置
    reset() {
      this.form = {
        sysUserId: this.userInfo.id,
        name: "",
        nickName: "",
        password: "",
        status: "1"
      };
      this.resetForm("form");
    },
    getUrl(openId) {
      let baseUrl = this.baseUrl;
      let url = baseUrl + "/de/bizUser/showFace/" + openId;
      return url;
    },
    handleChangeStatus(row, val) {
      let form = {
        id: row.id,
        status: val
      }
      update(form).then(res => {
        if (res.code == 200) {
          this.$modal.msgSuccess("操作成功");
          this.getList();
        }
      });
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.multiple = !selection.length != 0
    },
    // handleSelectionChange(selection) {
    //   this.ids = [];
    //   let flg = false;
    //   selection.forEach((ele) => {
    //     this.ids.push(ele.id);
    //     if (ele.status == 1) {
    //       flg = true;
    //     }
    //   });
    //   this.ids = selection.map((item) => item.id);
    //   this.single = selection.length != 1;
    //   this.multiple = !selection.length || flg;
    // },
    cancel() {
      this.open = false;
      this.form = {};
    },
    handleAdd() {
      this.reset();
      this.showPassword = true;
      if (!this.showSysUser) {
        this.form.sysUserId = this.userInfo.userId
      }
      this.open = true;
      this.title = "追加服务对象"
    },
    handleUpdate(row) {
      this.reset();
      this.showPassword = false;
      const id = row.id || this.ids;
      get(id).then(response => {
        this.form = response.data;
        this.form.status = this.form.status + "";
        this.open = true;
        this.title = "修改服务对象信息";
      });
    },
    handleDel(row) {
      const ids = row.id || this.ids;
      this.$confirm('此操作将永久删除选中的服务对象, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        del(ids).then(res => {
          if (res.code == 200) {
            this.$modal.msgSuccess("操作成功");
            this.open = false;
            this.getList();
          }
        })
      }).catch(() => {
        this.$modal.msg({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let form = {
            sysUserId: this.form.sysUserId,
            name: this.form.name,
            nickName: this.form.nickName,
            password: this.form.password,
            status: this.form.status,
          }
          if (this.form.id) {
            form.id = this.form.id
            update(form).then(res => {
              if (res.code == 200) {
                this.$modal.msgSuccess("操作成功");
                this.open = false;
                this.getList();
              }
            })
          } else {
            add(form).then(res => {
              if (res.code == 200) {
                this.$modal.msgSuccess("操作成功");
                this.open = false;
                this.getList();
              }
            })
          }
        }
      });
    },
    // 权限验证
    checkRoles() {
      let flg = 0;
      if (this.userInfo.roles) {
        //console.log("this.userInfo", this.userInfo.roles)
        this.userInfo.roles.forEach((role) => {
          if (role.roleKey == "admin") {
            flg = 9;
          } else if (role.roleKey == "subAdmin") {
            flg = flg > 3 ? flg : 3;
          } else if (role.roleKey == "other") {
            flg = flg > 2 ? flg : 2;
          } else if (role.roleKey == "company") {
            flg = flg > 1 ? flg : 1;
          }
        });
      }
      return flg;
    },
    //   重置密码
    handleChangePassword(row) {
      this.$prompt(`请输入 ${row.name || row.nickName}  的新密码`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        inputPattern: /^.{5,20}$/,
        inputErrorMessage: "用户密码长度必须介于 5 和 20 之间"
      }).then(({value}) => {
        const form = {
          userId: row.id,
          newPassword: value
        }
        updatePwd(form).then(response => {
          this.$modal.msgSuccess("修改成功，新密码是：" + value);
        });
      }).catch(() => {
      });
    }

  },
  created() {
    // 检测类型
    listData({dictType: 'biz_user_status'}).then(res => {
      if (res.code == 200) {
        this.statusOptions = res.rows
      }
    })
    listAll().then(res => {
      if (res.code == 200) {
        this.userOptions = res.data
      }
    })
    getInfo().then(res => {
      this.showSysUser = false;
      res.roles.forEach(ele => {
        if (ele == "admin" || ele == "common") {
          this.showSysUser = true;
        }
      })
      this.userInfo = res.user
      if (!this.showSysUser) {
        this.queryParams.sysUserId = res.user.userId
      }
      this.getList();
    })
  }
}
</script>

<style lang="scss" scoped>
.review-type {
  padding: 10px 20px;
  height: calc(100vh - 86px);
  overflow-y: auto;
}
</style>
<!--<style lang="scss">-->
<!--.image-table::before{-->
<!--  display: none;-->
<!--}-->
<!--.image-table {-->
<!--  .el-table__header-wrapper{-->
<!--    position: sticky;-->
<!--    top: 0;-->
<!--    z-index: 1;-->
<!--  }-->
<!--}-->
<!--</style>-->
