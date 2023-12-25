<template>
  <div class="app-container home" style="background: #f5f5f5">
    <el-row :gutter="20">
      <el-row :gutter="24">
        <el-col :span="24" :offset="0"
        ><h3 style="fontsize: 20px; color: #101010; paddingleft: 5px">
          <b>👏 欢迎回来，Ryan Septimus</b>
        </h3></el-col
        >
      </el-row>

      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card
          class="update-log"
          style="
            height: 200px;
            box-shadow: none;
            border: none;
            backgroundcolor: #fff;
          "
        >
          <div class="clearfix title">
            <span>资源概览</span>
          </div>
          <el-row :gutter="24">
            <el-col :span="12" :offset="0">
              <div class="echarts">
                <div
                  style="
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    /*marring-left: -20+'px';*/
                  "
                >
                  <div id="chartLeft" style="width: 150px; height: 120px"></div>
                  <div class="percentage" :style="{marginLeft:percentageLeft<10?'-92px':'-100px'}">{{ percentageLeft }}%</div>
                </div>
                <div class="desc" :style="{marginLeft:percentageLeft<10?'50':'35'}">
                  <div>数字员工数量</div>
                  <div><b>{{ resource.usedEmployeeAmount }} / {{ resource.employeeAmount | filterAmount }}</b></div>
                </div>
              </div>
            </el-col>
            <el-col :span="12" :offset="0">
              <div class="echarts">
                <div
                  style="
                    display: flex;
                    align-items: center;
                    marring-left: -20+'px';
                  "
                >
                  <div
                    id="chartRight"
                    style="width: 150px; height: 120px"
                  ></div>
                  <div class="percentage" :style="{marginLeft:percentageRight<10?'-92px':'-100px'}">{{ percentageRight }}%</div>
                </div>
                <div class="desc" :style="{marginLeft:percentageRight<10?'50':'35'}">
                  <div>上传文件数量</div>
                  <div><b>{{ resource.usedDocAmount }} / {{ resource.docAmount | filterAmount }}</b></div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card
          class="update-log"
          style="
            height: 200px;
            box-shadow: none;
            border: none;
            backgroundcolor: #fff;
          "
        >
          <div class="clearfix title">
            <span>快捷入口</span>
          </div>
          <div class="text item">
            <el-row type="flex" justify="space-between">
              <el-col
                :span="6"
                v-if="checkRoles() >= 3"
                type="flex"
                justify="center"
                align="middle"
                class="active"
              >
                <el-link
                  :underline="false"
                  style="color: #666666; padding: 20px 0px"
                  @click="gotoDigitalEmployees"
                >
                  <div style="width: 100%; text-align: center">
                    <!-- <i class="el-icon-s-custom" style="font-size: 80px"></i> -->
                    <img
                      src="@/assets/images/DigitalEmployeeManagement.png"
                      alt=""
                      width="60"
                    />
                    <br/>
                    <span style="font-weight: 400; font-size: 14px"
                    >数字员工管理</span
                    >
                  </div>
                </el-link>
              </el-col>
              <el-col
                :span="6"
                v-if="checkRoles() >= 3"
                type="flex"
                justify="center"
                align="middle"
                class="active"
              >
                <el-link
                  :underline="false"
                  style="color: #666666; padding: 20px 0px"
                  @click="gotoItem"
                >
                  <div style="width: 100%; text-align: center">
                    <!-- <i class="el-icon-s-opportunity" style="font-size: 80px"></i> -->
                    <img
                      src="@/assets/images/KnowledgeBaseManagement.png"
                      alt=""
                      width="60"
                    />
                    <br/>
                    <span style="font-weight: 400; font-size: 14px"
                    >知识库管理</span
                    >
                  </div>
                </el-link>
              </el-col>
              <el-col
                :span="6"
                v-if="checkRoles() >= 9"
                type="flex"
                justify="center"
                align="middle"
                class="active"
              >
                <el-link
                  :underline="false"
                  style="color: #666666; padding: 20px 0px"
                  @click="gotoCompany"
                >
                  <div style="width: 100%; text-align: center">
                    <img src="@/assets/images/USER.png" alt="" width="60"/>
                    <br/>
                    <span style="font-weight: 400; font-size: 14px"
                    >用户管理</span
                    >
                  </div>
                </el-link>
              </el-col>
              <el-col
                :span="6"
                v-if="checkRoles() >= 3"
                type="flex"
                justify="center"
                align="middle"
                class="active"
              >
                <el-link
                  :underline="false"
                  style="color: #666666; padding: 20px 0px"
                  @click="gotoServer"
                >
                  <div style="width: 100%; text-align: center">
                    <img
                      src="@/assets/images/DialogueCollage.png"
                      alt=""
                      width="60"
                    />
                    <br/>
                    <span style="font-weight: 400; font-size: 14px"
                    >服务对象管理</span
                    >
                  </div>
                </el-link>
              </el-col>
              <el-col
                :span="6"
                v-if="checkRoles() >= 9"
                type="flex"
                justify="center"
                align="middle"
                class="active"
              >
                <el-link
                  :underline="false"
                  style="color: #666666; padding: 20px 0px"
                  @click="gotoTemplate"
                >
                  <div style="width: 100%; text-align: center">
                    <img
                      src="@/assets/images/dataStatistics.png"
                      alt=""
                      width="60"
                    />
                    <br/>
                    <span style="font-weight: 400; font-size: 14px"
                    >员工模板管理</span
                    >
                  </div>
                </el-link>
              </el-col>
              <el-col
                :span="6"
                v-if="checkRoles() == 4"
                type="flex"
                justify="center"
                align="middle"
                class="active"
              >
                <el-link
                  :underline="false"
                  style="color: #666666; padding: 20px 0px"
                  @click="gotoDialogueList"
                >
                  <div style="width: 100%; text-align: center">
                    <img
                      src="@/assets/images/dataStatistics.png"
                      alt=""
                      width="60"
                    />
                    <br/>
                    <span style="font-weight: 400; font-size: 14px"
                    >对话详单</span
                    >
                  </div>
                </el-link>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin-left: 0px;margin-right: 0px;display: flex;align-items: center;justify-content: start;flex-wrap: wrap" :gutter="24">
      <div class="employeeItem" @click="details(item)" v-for="(item, index) in emplayeeArr" :key="index">
        <img :src="'data:image/png;base64,' + item.avatar" alt=""/>
        <p style="fontsize: 16px">
          <b>{{ item.templateName }} {{ item.name }}</b>
        </p>
        <p style="fontsize: 10px; color: #86909c" class="notes">
          <b>{{ item.notes }}</b>
        </p>
        <div class="mode">
          <p>
            今日服务次数
            <span style="color: #000; fontweight: 600">{{
                item.serveTimeToday
              }}</span>
          </p>
          <p>
            今日服务人数
            <span style="color: #000; fontweight: 600">{{
                item.serveUserToday
              }}</span>
          </p>
        </div>
      </div>
      <!--      </el-col>-->
    </el-row>
    <!--      </el-col>-->
    <!--    </el-row>-->
  </div>
</template>

<script>
// import {listNotice} from "@/api/system/notice";
import {getAllEmployee, getResource, list} from "@/api/employee/employee";
import {listData} from "@/api/system/dict/data";
import {getInfo} from "@/api/login";
import echarts from "echarts";
import {getToken} from "@/utils/auth";
import {listAll as listTemplates} from "@/api/template/template";

export default {
  name: "Index",
  data() {
    return {
      // 版本号
      noticeList: [],
      totalNotice: [],
      checkList: [],
      totalCheck: [],
      userInfo: {},
      version: "3.8.5",
      percentageLeft: 0, // 这里假设百分比是75%
      percentageRight: 0,
      emplayeeArr: [],
      windowWidthLayout: 3,
      resource: {
        "usedEmployeeAmount": 0,
        "employeeAmount": 0,
        "usedDocAmount": 0,
        "docAmount": 0
      },
      //   创建者
      createBy: "",
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
    filterAmount: function (val) {
      if (val == -1) return "无限制"
      return val
    }
  },
  updated() {
    getInfo().then(res => {
      this.createBy = res.user.userName
    });
    // 获取DOM元素
    const chartLeftContainer = document.getElementById("chartLeft");
    const chartRightContainer = document.getElementById("chartRight");
    // 初始化ECharts图表
    const chartLeft = echarts.init(chartLeftContainer);
    const chartRight = echarts.init(chartRightContainer);
    // ECharts配置项
    const optionLeft = {
      series: [
        {
          hoverAnimation: false,
          type: "pie",
          radius: ["80%", "100%"], // 圆环半径
          label: {
            show: false, // 不显示标签
          },
          data: [
            {
              value: this.percentageLeft, name: 'Category 1', itemStyle: {
                color: '#1D93AB', // 圆环的初始颜色
              },
            },
            {
              value: 100 - this.percentageLeft, name: 'Category 2', itemStyle: {
                color: '#E8E8E8'// 圆环的初始颜色
              }, emphasis: {
                itemStyle: {
                  color: '#f2f2f2', // 鼠标悬停时的颜色
                },
              },
            },
          ],
        },
      ],
    };
    const optionRight = {
      series: [
        {
          hoverAnimation: false,
          type: "pie",
          radius: ["80%", "100%"], // 圆环半径
          label: {
            show: false, // 不显示标签
          },
          data: [
            {
              value: this.percentageRight,
              itemStyle: {
                color: "#FF9800", // 圆环的颜色
              },
            },
            {
              value: 100 - this.percentageRight,
              itemStyle: {
                color: "#E8E8E8", // 剩余部分的颜色
              }, emphasis: {
                itemStyle: {
                  color: '#f2f2f2', // 鼠标悬停时的颜色
                },
              },
            },
          ],
        },
      ],
    };
    // 使用配置项绘制图表
    chartLeft.setOption(optionLeft);
    chartRight.setOption(optionRight);
  },
  created() {
    // console.log("windowsWidth:", window.outerWidth);
    // if (window.outerWidth > 1540) {
    //   this.windowWidthLayout = 5;
    // } else if (window.outerWidth < 1540) {
    //   this.windowWidthLayout = 5;
    // }
    // list()
    //   .then((res) => {
    //     if (res.code == 200) {
    //       console.log(res);
    //       let emplayeeArr = [];
    //       for (const item of res.rows) {
    //         if (emplayeeArr.length == 0) {
    //           const obj = {
    //             id: item.id,
    //             templateId: item.templateId,
    //             name: item.name,
    //             templateName: "",
    //             avatar: item.avatar,
    //             notes: "",
    //             serveTime: item.serveTime ? item.serveTime : "0",
    //             serveTimeToday: item.serveTimeToday ? item.serveTimeToday : "0",
    //           };
    //           emplayeeArr.push(obj);
    //         } else if (emplayeeArr.length > this.windowWidthLayout) {
    //           break;
    //         } else {
    //           let f = false;
    //           for (const emplayeeArrItem of emplayeeArr) {
    //             if (emplayeeArrItem.templateId != item.templateId) {
    //               f = true;
    //               if (emplayeeArr.length > this.windowWidthLayout) {
    //                 break;
    //               }
    //             }
    //           }
    //           if (f) {
    //             const obj = {
    //               id: item.id,
    //               templateId: item.templateId,
    //               name: item.name,
    //               templateName: "",
    //               avatar: item.avatar,
    //               notes: "",
    //               serveTime: item.serveTime ? item.serveTime : "0",
    //               serveTimeToday: item.serveTimeToday
    //                 ? item.serveTimeToday
    //                 : "0",
    //             };
    //             emplayeeArr.push(obj);
    //           }
    //         }
    //       }
    //       this.emplayeeArr = emplayeeArr;
    //       console.log("elployeeArr", emplayeeArr);
    //       return listAll();
    //     }
    //   })
    //   .then((res) => {
    //     if (res.code == 200) {
    //       console.log("templateList", res);
    //       for (const templateItem of res.data) {
    //         for (const employeeItem of this.emplayeeArr) {
    //           if (employeeItem.templateId == templateItem.id) {
    //             if (!employeeItem.avatar) employeeItem.avatar = templateItem.avatar;
    //             employeeItem.notes = templateItem.introduction;
    //             employeeItem.templateName = templateItem.name;
    //           }
    //         }
    //       }
    //     }
    //   });

    /*
    * 获取全部数字员工
    * */

    getAllEmployee().then((res) => {
      if (res.code == 200) {
        let obj = res.data;
        obj = obj.filter((item) => {
          console.log(item);
          return item.status != 2;
        });
        listTemplates().then((res) => {
          let newArr = [];
          for (const item of obj) {
            for (const templateItem of res.data) {
              if (item.templateId == templateItem.id) {
                let newObj = {
                  ...item,
                  templateName: templateItem.name,
                  templateDesc: templateItem.introduction,
                  avatar: item.avatar ? item.avatar : templateItem.avatar,
                  serveTimeToday: item.serveTimeToday ? item.serveTimeToday : "0",
                  serveUserToday: item.serveUserToday
                    ? item.serveUserToday
                    : "0",
                };
                newArr.push(newObj);
              }
            }
          }
          console.log("obj", newArr);
          this.emplayeeArr = newArr;
        });
        this.total = res.total;
      }
    });


    getInfo().then((res) => {
      this.userInfo = res.user;
    });
    // 检测记录状态
    listData({dictType: "chk_batch_status"}).then((res) => {
      if (res.code == 200) {
        this.statusOptions = res.rows;
      }
    });
    // this.getListNotice();
    this.getListCheck();

    //   获取资源信息
    getResource().then((res) => {
      if (res.code == 200) {
        this.resource = res.data;
        this.percentageLeft = Math.trunc((res.data.employeeAmount == -1 || res.data.employeeAmount == 0 ? 0 : res.data.usedEmployeeAmount / res.data.employeeAmount) * 100);
        this.percentageRight = Math.trunc((res.data.docAmount == -1 || res.data.docAmount == 0 ? 0 : res.data.usedDocAmount / res.data.docAmount) * 100);
      }
    })
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
    // getListNotice() {
    //   let param = {
    //     pageNum: 1,
    //     pageSize: 5,
    //   };
    //   listNotice(param).then((res) => {
    //     if (res.code == 200) {
    //       this.noticeList = res.rows;
    //       this.totalNotice = res.total;
    //     }
    //   });
    // },
    getListCheck() {
      let param = {
        pageNum: 1,
        pageSize: 5,
      };
      list(param).then((res) => {
        if (res.code == 200) {
          this.checkList = res.rows;
          this.totalCheck = res.total;
        }
      });
    },
    checkRoles() {
      let flg = 0;
      this.userInfo.roles.forEach((role) => {
        if (role.roleKey == "admin") {
          flg = 9;
        } else if (role.roleKey == "vip") {
          flg = flg > 1 ? flg : 4;
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
    gotoDigitalEmployees() {
      this.$router.push("/resource/employee");
    },
    gotoItem() {
      this.$router.push("/resource/kb");
    },
    gotoCompany() {
      this.$router.push("/system/user");
    },
    gotoServer() {
      this.$router.push("/resource/user");
    },
    gotoTemplate() {
      this.$router.push("/business/template");
    },
    gotoDialogueList() {
      this.$router.push("/resource/record");
    },
    // 数字员工详情
    details(item) {
      console.log(item);
      this.$router.push(
        `/details/index?id=${item.id}&templateId=${
          item.templateId
        }&header=${getToken()}&createBy=${this.createBy}`
      );
    },
  },
};
</script>

<style scoped lang="scss">
.title {
  padding: 10px 0px;
  text-align: left !important;
  font-size: 16px;
  color: #101010;
  font-weight: bold;
  margin-bottom: 0px !important;
}

.employeeItem:first-child {
  margin-left: 0px;
}

.employeeItem:nth-child(4n+1) {
  margin-left: 0px;
}


.employeeItem {
  width: calc((100% - 66px) / 4);
  background: #fff;
  padding: 30px;
  display: flex;
  height: 400px;
  flex-direction: column;
  margin-left: 22px;
  align-items: center;
  justify-content: space-between;
  text-align: left;
  margin-top: 22px;
  cursor: pointer;

  .notes {
    word-break: break-all;
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    min-height: 35px;
  }

  img {
    width: 140px;
    height: 140px;
    border-radius: 50%;
  }

  .mode {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-around;
    color: #86909c;
    font-size: 12px;
  }

  .btn {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;

    button {
      width: 100px;
      color: white;
      text-align: center;
      font-size: 20px;
      outline: none;
      border: none;
      background: rgb(22, 155, 223);
      border-radius: 5px;
      cursor: pointer;
    }

    button:active {
      opacity: 0.8;
    }
  }

  .el-card__body {
    display: none;
  }
}

.echarts {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;

  .desc {
    margin-left: 50px;
    height: 120px;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    font-size: 12px;
    color: #444444;
  }
}

.percentage {
  font-size: 25px;
  margin-left: -100px;
}

.home {
  min-height: 100vh;

  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}

.active {
  margin-bottom: 10px;
}

@media screen and (max-width: 1560px) {
  .employeeItem {
    width: calc((100% - 44px) / 3);
  }

  .employeeItem:nth-child(3n+1) {
    margin-left: 0px;
  }
  .employeeItem:nth-child(4n+1) {
    margin-left: 22px;
  }
  .employeeItem:nth-child(13n) {
    margin-left: 0px;
  }
  .employeeItem:first-child {
    margin-left: 0px;
  }
}

.active:hover {
  border: 1px solid rgb(228, 228, 228);
  background: rgb(249, 249, 249);
}

.active:hover span {
  color: #1d93ab;
}

//
//.employeeDiv {
//  margin-top: 20px;
//}
//
//.employeeDiv:nth-child(2n) {
//  margin-left: 20px;
//  margin-right: 20px;
//}


</style>
