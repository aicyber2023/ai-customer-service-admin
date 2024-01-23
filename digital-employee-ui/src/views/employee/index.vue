<template>
  <div class="review-type">
    <h3><b>我的智能客服</b></h3>
    <div class="nav">
      <div class="navItem">
        <el-tooltip
          class="box-item"
          effect="dark"
          :content="item.name"
          placement="top"
          v-for="(item, index) in navData"
          :key="index"
        >
          <span
            @click="getDigitalEmployeesList(item.id)"
            :class="selectNav == item.id ? 'spanActive' : ''"
          >{{ item.name }}</span
          >
        </el-tooltip>
      </div>
      <!--      <div class="search">-->
      <!--        <el-input placeholder="搜索" v-model.trim="search">-->
      <!--          <i-->
      <!--            slot="suffix"-->
      <!--            class="el-input__icon el-icon-search"-->
      <!--            style="cursor: pointer"-->
      <!--            @click="searchEmployee"-->
      <!--          ></i>-->
      <!--        </el-input>-->
      <!--      </div>-->
    </div>
    <div class="employeeNum">
      <span
      ><b>员工数 {{ this.total }} / {{ employeeAmount | filterAmount }}</b></span
      >
    </div>
    <el-row :gutter="24">
      <el-col :span="8" :offset="0" v-if="queryParams.pageNum==1">
        <el-card class="box-card createEmployeeCard">
          <div class="createEmployee" @click="createState = true">
            <img
              src="@/assets/images/add.png"
              style="width: 42px; height: 42px"
            />
            <span style="fontsize: 12px; margintop: 20px"
            >点击创建智能客服</span
            >
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" v-for="(item, index) in tableData" :key="index">
        <el-card class="box-card">
          <div class="userInfo" @click="details(item)">
            <div class="info">
              <div class="nameAndrole">
                <span>{{ item.name }}</span>
                <span>{{ item.templateName }}</span>
              </div>
              <div class="desc">
                {{ item.templateDesc }}
              </div>
            </div>
            <div class="userAvatar">
              <el-avatar
                :size="125"
                :src="getUrl(item.id)"
              ></el-avatar>
            </div>
          </div>
          <div class="information">
            <span class="text"
            ><b
            >服务次数
                <span style="color: #000000">{{ item.serveTime }}</span></b
            ></span
            >
            <span class="text"
            ><b
            >文件个数
                <span style="color: #000000">{{
                    item.knowledgeBaseFileCount
                  }}</span></b
            ></span
            >
            <span class="text kb" @click="gotoKb(item.id)"
            ><b
            >管理知识库
                </b
                ></span
            >

            <span
              class="delete"
              @click="
                deleteState = true;
                selectDeleteStaff = item.id;
              "
            ><i
              class="el-icon-delete"
              style="fontsize: 20px; padding: 10px"
            ></i
            ></span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="23" :offset="0">
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>
    <!-- 创建智能客服弹出层 -->
    <el-dialog :visible.sync="createState" width="60%" @close="reset">
      <el-form
        :model="form"
        ref="form"
        :rules="rules"
        label-width="80px"
        :inline="false"
        size="normal"
      >
        <div class="myDialog-body">
          <span class="myDialog-title">创建智能客服</span>
          <div class="myDialog-content">
            <!-- 数字人名称 -->
            <div class="">
              <el-form-item
                label="智能客服昵称"
                size="medium"
                prop="name"
                label-width="120px"
                style="textalign: left"
              >
                <el-input
                  placeholder="请输入智能客服昵称"
                  v-model="form.name"
                  clearable
                  maxlength="16"
                  show-word-limit
                  style="minwidth: 100%"
                >
                </el-input>
              </el-form-item>
            </div>
            <!-- 公司昵称 -->
            <div class="myDialog-content-item">
              <span class="input_name">公司昵称</span>
              <el-input
                placeholder="请输入公司昵称"
                v-model="form.companyName"
                clearable
                maxlength="8"
                show-word-limit
              >
              </el-input>
            </div>
            <!-- 说话风格 -->
            <!-- <div class="myDialog-content-item">
            <span class="input_name">说话风格</span>
            <el-select v-model="selectSpeakingStyle" placeholder="请选择说话风格" size="medium" style="width:100%">
              <el-option
                v-for="item in speakingStyle"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </div> -->
            <!-- 客服模板 -->

            <div class="myDialog-content-item" style="align-items: start">
              <span class="input_name">客服模板</span>
              <el-scrollbar class="createEmplayeeScrollbar">
                <div class="jobFunctionContent">
                  <div
                    class="jobFunctionContentItem"
                    :class="
                      form.templateId == item.id
                        ? 'jobFunctionContentItemActive'
                        : ''
                    "
                    v-for="item in createEmployeeTemplateList"
                    :key="item.id"
                    @click="form.templateId = item.id"
                  >
                    <span class="career">{{ item.name }}</span>
                    <span class="desc">{{ item.introduction }}</span>
                  </div>
                </div>
              </el-scrollbar>
            </div>

          </div>
          <span slot="footer" class="myDialog-footer">
            <el-button class="editConfirm" type="primary" @click="submitForm"
            >确 定</el-button
            >
            <el-button class="editClose" @click="createState = false"
            >取 消</el-button
            >
          </span>
        </div>
      </el-form>
    </el-dialog>
    <!-- 删除按钮对话框 -->
    <el-dialog title="提示" :visible.sync="deleteState" width="30%">
      <span>确认删除智能客服？</span>
      <span slot="footer" class="dialog-footer">
        <el-button class="editClose" @click="deleteState = false"
        >取 消</el-button
        >
        <el-button class="editConfirm" type="primary" @click="deleteStaff"
        >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {add, del, get, getResource, list, update,} from "@/api/employee/employee";
import {get as selectTemplate, listAll as listTemplates, selectUsedTemplateList} from "@/api/template/template";
import {listAll as listUsers} from "@/api/system/user";
import {listData} from "@/api/system/dict/data";
import {getInfo} from "@/api/login";
import {getToken} from "@/utils/auth";

export default {
  name: "index",
  data() {
    return {
      baseUrl: window.cfg.baseUrl,
      // 选中的说话风格
      selectSpeakingStyle: "",
      // 新建员工对话框的状态
      createState: false,
      // 搜索框的值
      search: "",
      // 导航按钮的数据
      navData: [],
      // 员工总数量
      employeeNum: 0,
      //选中的tab标题id
      selectNav: 0,
      loading: false,
      tableData: [],
      templateOptions: [],
      userOptions: [],
      statusOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 12,
        sysUserId: undefined,
        templateId: undefined,
        name: "",
        status: undefined,
      },
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      showSearch: true,
      showSysUser: false,
      title: "新增安全员信息",
      open: false,
      userInfo: {},
      form: {
        name: "",
        sysUserId: undefined,
        templateId: undefined,
        greeting: "",
        companyName: "",
        status: "0",
      },
      rules: {
        name: [{required: true, message: "请输入员工昵称", trigger: "blur"}],
      },
      // 删除按钮提示框
      deleteState: false,
      // 选中要删除的智能客服
      selectDeleteStaff: null,
      // 智能客服的数量
      total: 24,
      //   总智能客服数量
      employeeAmount: 0,
      //   创建智能客服的模板
      createEmployeeTemplateList: [],
      //   创建者 账号
      createBy: "",
      //   选中的模板信息
      templateData: {},
    };
  },
  filters: {
    filterStage: function (val, list) {
      if (list && list.length != 0) {
        return list.filter((v) => v.dictValue == val)[0].dictLabel;
      }
      return val;
    },
    filterCss: function (val, list) {
      if (list && list.length != 0) {
        return list.filter((v) => v.dictValue == val)[0].listClass;
      }
      return val;
    },
    filterUser: function (val, list) {
      if (list && list.length != 0) {
        return list.filter((v) => v.userId == val)[0].nickName;
      }
      return val;
    },
    filterTemplate: function (val, list) {
      if (list && list.length != 0) {
        return list.filter((v) => v.id == val)[0].name;
      }
      return val;
    },
    filterAmount: function (val) {
      if (val == -1) return "无限制"
      return val
    },
  },
  methods: {

    // // 模糊查询智能客服
    // searchEmployee() {
    //   if (this.search != "") {
    //     const form = {
    //       name: this.search,
    //     };
    //     this.getList(form);
    //   }
    // },
    // 删除智能客服
    deleteStaff() {
      this.deleteState = false;
      //console.log("删除:" + this.selectDeleteStaff);
      del(this.selectDeleteStaff).then((res) => {
        if (res.code == 200) {
          //console.log("删除成功");
          this.getList();
        }
      });
    },
    getUrl(id) {
      let baseUrl = this.baseUrl;
      //console.log("baseurl:", baseUrl);
      let url = baseUrl + "/de/digitalEmployee/showAvatar/" + id;
      //console.log("url", url);
      return url;
    },
    getList(form = this.queryParams) {
      const queryParams = form.limit ? this.queryParams : form;
      //console.log("queryParams", queryParams);
      list(queryParams).then((res) => {
        if (res.code == 200) {
          let obj = res.rows;
          obj = obj.filter((item) => {
            //console.log(item);
            return item.status != 2;
          });
          listTemplates().then((res) => {
            this.createEmployeeTemplateList = (res.data).filter(item => {
              return item.status == 1
            });
            this.form.templateId = this.createEmployeeTemplateList[0].id;
            this.templateData = this.createEmployeeTemplateList[0];
            let newArr = [];
            for (const item of obj) {
              for (const templateItem of res.data) {
                if (item.templateId == templateItem.id) {
                  let newObj = {
                    ...item,
                    templateName: templateItem.name,
                    templateDesc: templateItem.introduction,
                    avatar: item.avatar ? item.avatar : templateItem.avatar,
                  };
                  newArr.push(newObj);
                }
              }
            }
            //console.log("obj", newArr);
            this.tableData = newArr;
            if (!form.templateId) {
              this.employeeNum = newArr.length;
            }
          });
          this.total = res.total;
        }
      });
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
        this.queryParams.sysUserId = this.userInfo.userId;
      }
      this.queryParams.templateId = undefined;
      this.queryParams.name = "";
      this.queryParams.status = undefined;
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let form = {
            name: this.form.name,
            sysUserId: this.userInfo.userId,
            templateId: this.form.templateId,
            greeting: this.form.greeting,
            status: 0,
            companyName: this.form.companyName,
          };
          //console.log("上传的form", form);
          if (this.form.id) {
            form.id = this.form.id;
            update(form).then((res) => {
              if (res.code == 200) {
                this.$modal.msgSuccess("修改成功");
                this.createState = false;
                this.getList();
              }
            });
          } else {
            selectTemplate(this.form.templateId).then(res => {
              if (res.code == 200) {
                for (const item of res.data.context) {
                   delete item .id
                }
                for (const item of res.data.procedureList) {
                  delete item .id
                }
                const form = {
                  name: this.form.name,
                  avatar: res.data.avatar,
                  avatarContentType: res.data.avatarContentType,
                  companyName: this.form.companyName,
                  companyAvatar: res.data.companyAvatar,
                  companyAvatarContentType: res.data.companyAvatarContentType,
                  context:res.data.context,
                  temperature: res.data.temperature,
                  presencePenalty: res.data.presencePenalty,
                  frequencyPenalty: res.data.frequencyPenalty,
                  historyMessageCount: res.data.historyMessageCount,
                  sysUserId: this.userInfo.userId,
                  templateId: this.form.templateId,
                  greeting: this.form.greeting,
                  status: 0,
                  procedureList: res.data.procedureList,
                  chatType:res.data.chatType,
                  qaRadius:res.data.qaRadius,
                  kbRadius:res.data.kbRadius,
                  modelSwitch:res.data.modelSwitch,
                }
                add(form).then((res) => {
                  if (res.code == 200) {
                    this.$modal.msgSuccess("创建成功");
                    this.createState = false;
                    this.getList();
                  }
                });
              }
            })

          }
        }
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    handleAdd() {
      this.reset();
      if (!this.showSysUser) {
        this.form.sysUserId = this.userInfo.userId;
      }
      this.open = true;
      this.title = "新增安全员信息";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      get(id).then((response) => {
        this.form = response.data;
        this.form.status = this.form.status + "";
        this.open = true;
        this.title = "修改安全员信息";
      });
    },
    handleDel(row) {
      const ids = row.id || this.ids;
      this.$confirm("此操作将永久删除选中的安全员信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          del(ids).then((res) => {
            if (res.code == 200) {
              this.$modal.msgSuccess("操作成功");
              this.open = false;
              this.getList();
            }
          });
        })
        .catch(() => {
          this.$modal.msg({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    // 表单重置
    reset() {
      this.form = {
        name: "",
        sysUserId: undefined,
        templateId: this.createEmployeeTemplateList[0].id,
        greeting: "",
        status: "0",
      };
      this.resetForm("form");
    },
    checkRoles() {
      let flg = 0;
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
      return flg;
    },
    // 获取指定的智能客服列表
    getDigitalEmployeesList(templateId) {
      this.selectNav = templateId;
      //console.log(templateId);
      let form = {
        ...this.queryParams,
      };
      if (templateId != 0) {
        form = {
          ...this.queryParams,
          templateId,
        };
      }
      //console.log(form);
      this.getList(form);
    },
    // 智能客服详情
    details(item) {
      window.sessionStorage.setItem("index",1)
      //console.log(item);
      this.$router.push(
        `/details/index?id=${item.id}&templateId=${
          item.templateId
        }&header=${getToken()}&createBy=${this.createBy}`
      );
    },
    gotoKb(id) {
      //   跳转到原先的知识库页面
      this.$router.push({
        path: "/resource/kb", query: {
          id: id
        }
      });
    }
  },
  created() {
    getInfo().then(res => {
      this.createBy = res.user.userName
    });
    listUsers().then((res) => {
      if (res.code == 200) {
        this.userOptions = res.data;
      }
    });
    listData({dictType: "biz_employee_status"}).then((res) => {
      if (res.code == 200) {
        this.statusOptions = res.rows;
      }
    });
    getInfo().then((res) => {
      this.showSysUser = false;
      res.roles.forEach((ele) => {
        if (ele == "admin" || ele == "common") {
          this.showSysUser = true;
        }
      });
      this.userInfo = res.user;
      if (!this.showSysUser) {
        this.queryParams.sysUserId = res.user.userId;
      }
      return selectUsedTemplateList()
    }).then((res) => {
      if (res.code == 200) {
        this.templateOptions = {...res.data};
        //console.log(res.data);
        this.form.templateId = res.data[0].id;
        const obj = {
          id: 0,
          name: "全部",
        };
        this.navData = res.data;
        this.navData.unshift(obj);
      }
    });
    this.getList();
    getResource()
      .then(res => {
        if (res.code == 200) {
          this.employeeAmount = res.data.employeeAmount
        }
      })
  },
};
</script>

<style lang="scss">
.el-avatar {
  background: none !important;
}

.el-tooltip__popper {
  background: #e7f8ff !important;
  color: #1d93ab !important;

  .popper__arrow::after {
    border-bottom-color: #1d93ab !important;
    border-top-color: #1d93ab !important;
  }
}

.el-pagination {
  .number:hover {
    color: #1d93ab !important;
  }

  .active:hover {
    color: white !important;
  }
}

//.el-button--text {
//  color: #1d93ab !important;
//
//  i {
//    color: #1d93ab !important;
//  }
//
//  &:hover {
//    color: #8bb4bc !important;
//
//    i {
//      color: #1d93ab !important;
//    }
//  }
//}

.box-card:hover {
  background: rgb(231, 248, 255);
}

.el-dialog__headerbtn i:hover {
  color: #1d93ab !important;
}

.el-input__inner:focus {
  border-color: #1d93ab !important;
}

.editConfirm {
  color: #1d93ab;
  background-color: #e7f8ff;
  border-color: #1d93ab;
}

.editConfirm:hover,
.editConfirm:focus {
  background: #b6dcec;
  border-color: #117e93;
  color: #1d93ab;
}

.editClose {
  color: #1d93ab;
}

.editClose:hover,
.editClose:focus {
  background: #b6dcec;
  border-color: #117e93;
  color: #1d93ab;
}

// Dialog设置边框
// 对话框圆角
.el-dialog__wrapper {
  .el-dialog {
    border-radius: 30px;
  }
}

.myDialog-body {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;

  .myDialog-title {
    font-size: 20px;
    font-family: "PingFang SC";
    color: #444444;
    margin-bottom: 20px;
  }

  .myDialog-content {
    width: 80%;

    .createEmplayeeScrollbar {
      width: 100%;
      height: 400px;
      max-height: 400px;

      .el-scrollbar__wrap {
        overflow-x: hidden;
      }

      // overflow-x: none;
      // .is-horizontal::-webkit-scrollbar-track{
      //   background: white !important;
      // }
    }

    .myDialog-content-item {
      width: 100%;
      display: flex;
      align-items: center;
      margin: 20px 0px;

      .input_name {
        text-align: center;
        width: 126px;
        margin-right: 15px;
        color: #606266;
        font-size: 14px;
        font-weight: bold;
      }

      .jobFunctionContent {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: start;
        flex-wrap: wrap;

        .jobFunctionContentItem {
          margin-right: 12px;
          width: calc((100% - 24px) / 3);
          min-height: 100px;
          padding: 20px;
          margin-bottom: 2%;
          border: 1px solid #e5e8ef;
          border-radius: 10px;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          cursor: pointer;

          .career {
            font-size: 14px;
            color: black;
            font-weight: bold;
            margin-bottom: 10px;
          }

          .desc {
            font-size: 12px;
            color: #86909c;
            word-break: break-all;
            overflow: hidden;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
          }
        }

        .jobFunctionContentItem:nth-child(3n + 3) {
          margin-right: 0px;
        }

        .jobFunctionContentItemActive {
          border-color: #1d93ab;
        }
      }
    }
  }

  .myDialog-footer {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-evenly;
    margin-top: 10px;
  }
}

.review-type {
  padding: 10px 20px;
  height: calc(100vh - 86px);
  overflow-y: auto;

  h3 {
    margin: 30px 0px;
  }

  .nav {
    width: 100%;
    display: flex;
    align-items: flex-start;
    justify-content: space-between;

    .navItem {
      display: flex;
      align-items: center;
      flex-wrap: wrap;

      span {
        width: 110px;
        padding: 10px 20px;
        margin-bottom: 10px;
        text-align: center;
        border-radius: 32px;
        margin-right: 10px;
        color: #4e5969;
        cursor: pointer;
        // 一行超过省略号
        text-overflow: ellipsis;
        overflow: hidden;
        word-break: break-all;
        white-space: nowrap;

        &:hover {
          padding: 10px 20px;
          background: rgb(231, 248, 255);
        }
      }

      .spanActive {
        background: #1d93ab;
        color: white;
      }

      .spanActive:hover {
        background: #1d93ab;
        color: white;
      }
    }

    .search {
      flex: 3;
    }

    .el-input__inner {
      background: #f2f3f8;
    }
  }

  .employeeNum {
    margin: 20px 0px;
  }

  .createEmployeeCard {
    display: flex;
    justify-content: center;
    align-items: center;
    color: #86909c;
    cursor: pointer;

    .createEmployee {
      display: flex;
      flex-direction: column;
      align-items: center;
      font-size: 25px;
    }
  }

  .box-card {
    width: 100%;
    height: 280px;
    box-shadow: none;
    margin-bottom: 20px;
    cursor: pointer;
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;

    .userInfo {
      width: 100%;
      display: flex;

      .info {
        min-width: 60%;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        margin-right: 20px;

        .nameAndrole {
          font-size: 14px;
          font-weight: bold;

          span {
            margin-right: 10px;
          }
        }
      }

      .desc {
        color: #86909c;
        font-size: 10px;
      }
    }

    .information {
      width: 100%;
      margin-top: 20px;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .text {
        color: #86909c;
        font-size: 12px;
      }

      .kb {
        color: #1d93ab;
      }

      .delete:hover {
        color: #1d93ab;
      }
    }
  }
}
</style>
