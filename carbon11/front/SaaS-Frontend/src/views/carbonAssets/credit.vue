<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="myassets-div">
          <img class="icon" src="@/assets/icon/icon_my_assets.png" />
          <span style="margin-left: 8px" class="list-blue-text text-left"
            >我的碳信用资产</span
          >
        </div>
        <div class="myassets-container">
          <span class="assets-hint">持仓总量</span>
          <span class="assets-text">{{ setNumber(topData.total) }}(tCO2e)</span>
          <div class="assets-line" />
          <span class="assets-hint">可用数量</span>
          <span class="assets-text">{{ setNumber(topData.availableAmount) }}(tCO2e)</span>
          <div class="assets-line" />
          <span class="assets-hint">锁定数量</span>
          <span class="assets-text">{{ setNumber(topData.lockedAmount) }}(tCO2e)</span>
          <div class="assets-line" />
          <span class="assets-hint">冻结数量</span>
          <span class="assets-text">{{ setNumber(topData.frozenAmount) }}(tCO2e)</span>
          <div class="empty-holder" />
          <button
            style="width: 68px; margin-left: 16px"
            class="normal-blue-btn"
            @click="onClickUpload"
          >
            上传
          </button>
          <button
            style="width: 68px; margin-left: 16px"
            class="normal-white-btn center-vertical"
            @click="onClickBuy"
          >
            我想买
          </button>
        </div>
        <div class="container">
          <div class="selectbox-root">
            <a class="selectbox-hint" style="min-width: 80px">核证标准</a>
            <div class="selectbox-deliver" />
            <el-cascader
              placeholder="全部"
              class="selectbox-input"
              :options="optionsStandard"
              v-model="seletedStandard"
              clearable
              @change="update"
            >
            </el-cascader>
          </div>
          <div style="margin-left: 16px" class="selectbox-root">
            <a class="selectbox-hint" style="min-width: 60px">领域</a>
            <div class="selectbox-deliver" />
            <el-cascader
              placeholder="全部"
              class="selectbox-input"
              :options="optionsIndustory"
              v-model="seletedIndustry"
              @change="update"
              clearable
            >
            </el-cascader>
          </div>
          <div style="margin-left: 16px" class="selectbox-root">
            <a class="selectbox-hint" style="min-width: 80px">交易状态</a>
            <div class="selectbox-deliver" />
            <el-cascader
              placeholder="全部"
              class="selectbox-input"
              :options="optionsOnlines"
              v-model="seletedStatus"
              clearable
              @change="update"
            >
            </el-cascader>
          </div>
          <div style="margin-left: 16px" class="selectbox-root">
            <a class="selectbox-hint" style="min-width: 80px">资产状态</a>
            <div class="selectbox-deliver" />
            <el-cascader
              placeholder="全部"
              class="selectbox-input"
              :options="optionsAssetStatus"
              v-model="seletedAssetsStatus"
              clearable
              @change="update"
            >
            </el-cascader>
          </div>
          <div
            style="padding-right: 0px; margin-left: 16px"
            class="selectbox-root"
          >
            <a class="selectbox-hint" style="min-width: 80px">签发日期</a>
            <div class="selectbox-deliver" />
            <el-date-picker
              v-model="selectDate"
              type="datetime"
              placeholder="选择开始时间"
              align="right"
              size="medium"
              :picker-options="pickerOptions"
              value-format="yyyy-MM-dd HH:mm:ss"
              @change="update"
            >
            </el-date-picker>
            <el-date-picker
              v-model="selectEndDate"
              type="datetime"
              placeholder="选择结束时间"
              align="right"
              size="medium"
              :picker-options="pickerOptions"
              value-format="yyyy-MM-dd HH:mm:ss"
              @change="update"
            >
            </el-date-picker>
          </div>
        </div>
        <div style="clear: both; height: 10px"></div>
        <div class="container">
          <div style="margin-right: 16px" class="selectbox-root">
            <a class="selectbox-hint" style="min-width: 100px">按项目搜索</a>
            <div class="selectbox-deliver" />
            <el-input
              v-model="searchProjectKeyword"
              placeholder="输入项目名称"
              clearable
              size="medium"
              @clear="searchByProject"
              @keyup.enter.native="searchByProject"
            />
          </div>
          <button class="light-green-btn" @click="searchByProject">查询</button>
          <div style="margin-left: 16px" class="selectbox-root">
            <a class="selectbox-hint" style="min-width: 100px">方法学搜索</a>
            <div class="selectbox-deliver" />
            <el-input
              v-model="searchMethodKeyword"
              placeholder="输入方法学名称"
              clearable
              size="medium"
              @clear="searchByMethod"
              @keyup.enter.native="searchByMethod"
            />
          </div>
          <button
            class="light-green-btn"
            @click="searchByMethod"
            style="margin-left: 16px"
          >
            查询
          </button>
        </div>
        <div>
          <el-table
            :header-cell-style="{
              background: '#F2F5F7',
              border: '0px solid #DDDDDD',
              color: '#242B35',
              height: '64px',
            }"
            :show-header="true"
            :data="list"
            stripe
            :row-style="{ height: '64px' }"
            :cell-style="cellStyle"
            @cell-click="handle"
            style="width: 100%"
          >
            <el-table-column min-width="10"></el-table-column>
            <el-table-column label="序号" align="left" min-width="40">
              <template slot-scope="scope"
                ><span>{{ getCurListNo(scope.$index) }}</span></template
              >
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="projectName"
              align="left"
              label="项目名称"
              min-width="80"
            />
            <el-table-column
              align="left"
              prop="certificationCriteriaName"
              label="核证标准"
              min-width="100"
            />
            <el-table-column
              align="left"
              prop="total"
              label="持仓量(tCO2e)"
              min-width="60"
            >
            <template slot-scope="scope">
              <span>{{ setNumber(scope.row.total) }}</span>
            </template>
            </el-table-column>
            <!-- <el-table-column
              align="left"
              prop="availableAmount"
              label="可用量(tCO2e)"
              min-width="90"
            /> -->
            <el-table-column
              align="left"
              prop="valuation"
              label="资产估值(¥)"
              min-width="60"
            >
            <template slot-scope="scope">
              <span>{{ setNumber(scope.row.valuation) }}</span>
            </template>
            </el-table-column>
            <el-table-column
              align="left"
              prop="projectScopeType"
              label="类型"
              min-width="100"
            />
            <el-table-column
              align="left"
              prop="assetsStatusName"
              label="资产状态"
              min-width="60"
            />
            <!-- <el-table-column
              align="left"
              prop="transactionStatusName"
              label="交易状态"
              min-width="60"
            /> -->
            <el-table-column
              align="left"
              prop="issuingDate"
              label="签发日期"
              min-width="60"
            />
            <el-table-column label="操作" min-width="150" align="center">
              <template slot-scope="scope">
                <a class="list-blue-text" @click="toDetail(scope.row.id)"
                  >查看</a
                >
                <a
                  style="margin-left: 10px"
                  class="list-blue-text"
                  @click="OutsizeTransaction(scope.row)"
                  >场外上架</a
                >
                <a
                  style="margin-left: 10px"
                  class="list-green-text"
                  @click="insideTransaction(scope.row.id)"
                  >场内交易</a
                >
                <a
                  style="margin-left: 10px"
                  class="list-yello-text"
                  @click="onClickEdit(scope.row)"
                  >修改</a
                >
                <a
                  style="margin-left: 10px"
                  :class="offlineStyleChange(scope.row.assetsStatus)"
                  @click="onClickDel(scope.row.id)"
                  >删除</a
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div style="margin-top: 30px; margin-bottom: 10px" class="pageBox">
        <div style="flex-grow: 1" />
        <el-pagination
          style="margin: auto"
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="current"
          :page-size="pageSize"
          :page-count="pageCount"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        >
        </el-pagination>
      </div>
      <!-- 场外交易按钮弹出页面 -->
      <el-dialog :title="title" :visible.sync="dialogFormVisible" width="720px">
        <el-form label-position="left" label-width="130px" :model="form">
          <el-form-item label="出售数量(tCO2e)" prop="tradeQuantity">
            <span class="require">*</span>
            <el-input
              v-model="form.tradeQuantity"
              size="medium"
              style="width: 268px; top: -5px"
            ></el-input>
          </el-form-item>
          <el-form-item label="出售单价(￥)" prop="assetUnitPrice">
            <el-input
              v-model="form.assetUnitPrice"
              size="medium"
              style="width: 268px; top: -5px; margin-left: 10px"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="出售截止时间" prop="expirationDate">
            <el-date-picker
              type="date"
              placeholder="选择日期"
              size="medium"
              v-model="form.expirationDate"
              value-format="yyyy-MM-dd"
              style="width: 268px; top: -5px; margin-left: 10px"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="期望交割时间" prop="deliveryTime">
            <el-date-picker
              type="date"
              placeholder="选择日期"
              size="medium"
              v-model="form.deliveryTime"
              value-format="yyyy-MM-dd"
              style="width: 268px; top: -5px; margin-left: 10px"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="期望交割方式" prop="deliveryMethod">
            <el-select
              v-model="form.deliveryMethod"
              placeholder="协议转入、竞价交易、定价交易"
              size="medium"
              style="width: 536px; top: -5px; margin-left: 10px"
            >
              <el-option
                v-for="(item, index) in tradeMethods"
                :key="index"
                :label="item.name"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="期望交割场所" prop="deliveryExchange">
            <el-select
              v-model="form.deliveryExchange"
              placeholder="全国碳排放权交易中心、北京环境交易所、上海环境能源交易所"
              size="medium"
              style="width: 536px; top: -5px; margin-left: 10px"
            >
              <el-option
                v-for="(item, index) in exchangeList"
                :key="index"
                :label="item.name"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button
            type="primary"
            @click="submit('form')"
            class="light-green-btn"
            >确 定</el-button
          >
        </div>
      </el-dialog>
      <el-dialog title="上架成功" :visible.sync="showQuotation" width="30%">
        <span
          >您的采购单已提交，可在供需行情中查看。确定为您跳转供需行情沟通</span
        >
        <span slot="footer" class="dialog-footer">
          <el-button @click="showQuotation = false">取 消</el-button>
          <el-button @click="toQuotation" type="primary">确 定</el-button>
        </span>
      </el-dialog>
      <BuyAssets
        :dialogFormVisible="buyAssetsDlg"
        @changeBuyAssetsDialogFormVisible="changeDialogFormVisible"
      />
      <carbonUploadVue
        :dialogFormVisible="carbonUploadDlg"
        title="碳信用项目上传"
        :isCredit="true"
        :row="rowData"
        @changeVisible="changeCarbonVisible"
        @submit="submited"
        :isEdit="isEdit"
      />
    </div>
  </div>
</template>
<script>
import { loadCarbonCreditPageList, delCredit } from "@/api/carbonAssetApi";
import { setListNo } from "@/libs/public";
import {
  getExchangeDict,
  getDiliveryMethodeDict,
  getCertificationCriteriaDict,
  getBusinessDict,
  getAssetTradeStatusDict,
  getAssetStatusDict,
  getProjectAreaDict,
} from "@/config/dictHelper";
import * as credit from "@/api/carbonAssetApi";
import BuyAssets from "@/views/carbonTrade/quotation/buyAssets.vue";
import carbonUploadVue from "./carbonUpload.vue";
import { setLargeNumber } from "@/libs/public"
export default {
  name: "credit",
  components: { BuyAssets, carbonUploadVue },
  data() {
    return {
      indeterminateFlag: false, //表头复选框状态
      reRender: true, // 重新渲染列表使用
      allchecked: false,
      searchProjectKeyword: "",
      rowData: {},
      isEdit: false,
      seletedAssetsStatus: "",
      searchMethodKeyword: "",
      showBuyAssets: false, //是否显示购买资产
      list: [],
      total: 0,
      carbonUploadDlg: false,
      current: 1,
      topData: {
        total: 0,
        availableAmount: 0,
        frozenAmount: 0,
        lockedAmount: 0,
      },
      pageCount: 1,
      exchangeList: [
        {
          name: "123",
          value: "test",
        },
        {
          name: "123",
          value: "test",
        },
        {
          name: "123",
          value: "test",
        },
        {
          name: "123",
          value: "test",
        },
      ],
      tradeMethods: [],
      buyAssetsDlg: false,
      pageSize: 10,
      seletedStandard: "", //当前选择的标准
      seletedIndustry: "", //当前选择的行业
      seletedStatus: "", //当前选择的状态
      optionsStandard: [],
      optionsOnlines: [],
      optionsIndustory: [],
      optionsAssetStatus: [],
      //校验规则
      rules: {
        tradeQuantity: [
          { required: true, message: "请输入出售数量", trigger: "blur" },
        ],
        // negotiatedPrice: [
        //   { required: true, message: "请输入出售单价", trigger: "blur" },
        // ],
        // expirationDate: [
        //   {
        //     type: "date",
        //     required: true,
        //     message: "请选择日期",
        //     trigger: "change",
        //   },
        // ],
        // deliveryTime: [
        //   {
        //     type: "date",
        //     required: true,
        //     message: "请选择时间",
        //     trigger: "change",
        //   },
        // ],
        // deliveryMethod: [
        //   {
        //     required: true,
        //     message: "请选择交割方式",
        //     trigger: "change",
        //   },
        // ],
        // deliveryExchange: [
        //   {
        //     required: true,
        //     message: "请选择交割场所",
        //     trigger: "change",
        //   },
        // ],
      },
      labelPositio: "left",
      sellCarbonTotal: 0, //当前持仓量
      value: "",
      pickerOptions: {
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              picker.$emit("pick", new Date());
            },
          },
          {
            text: "昨天",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            },
          },
          {
            text: "一周前",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            },
          },
        ],
      },
      selectDate: "",
      selectEndDate: "",
      flag: 0, //防抖
      dialogFormVisible: false, //控制场外交易界面显示,
      form: {
        id: 0,
        tradeQuantity: "", //出售数量
        assetUnitPrice: "", //出售单价
        expirationDate: "", //出售截止时间
        deliveryTime: "", //期望交割时间
        deliveryMethod: "0190000000", //期望交割方式
        deliveryExchange: "0170000000", //期望交割地点
        projectType: "", //项目类型
        tradeRole: "0270000002", //方向
        status: "",
        projectId: null,
        assetType: "0140000001", //资产类型
        institutionName: "",
      },
      title: "",
      showQuotation: false,
    };
  },
  // mounted() {
  //   this.getList(1)
  // },
  methods: {
    setNumber(str){
      return setLargeNumber(str)
    },
    cellStyle({ row, column, rowIndex, columnIndex }) {
      if (column.label != "操作") {
        return "cursor:pointer;";
      }
    },
    offlineStyleChange(status) {
      if (status == "0130000004" || status == "0130000005") {
        return "list-red-text";
      } else {
        return "afterSubmitOffline";
      }
    },
    /**
     * 作者:
     * 时间: 2022-06-16 16:18:16
     * 功能: 跳转资产上传页面
     */
    onClickUpload() {
      this.isEdit = false;
      this.carbonUploadDlg = true;
    },
    getCurListNo(index) {
      let curNo = parseInt(index) + 1;
      let size = this.size || "10";
      let page = this.current - 1;
      let no = setListNo(page, size, curNo);
      return no ? no : 1;
    },
    /**
     * 作者:
     * 时间: 2022-06-21 11:52:27
     * 功能: 修改项目
     */
    onClickEdit(row) {
      this.isEdit = true;
      this.carbonUploadDlg = true;

      this.rowData = JSON.parse(JSON.stringify(row));
    },
    /**
     * 作者:
     * 时间: 2022-06-21 11:52:43
     * 功能: 删除项目
     */
    onClickDel(id) {
      // this.$confirm("")
      this.$confirm("删除内容不可复原，请谨慎操作").then((res) => {
        delCredit(Number(id))
          .then((res) => {
            this.$message.success("删除成功");
            this.getList(1);
          })
          .catch((err) => {
            this.$message.error("删除失败");
          });
      });
    },
    changeDialogFormVisible(res) {
      this.buyAssetsDlg = res;
    },
    changeCarbonVisible(res) {
      this.carbonUploadDlg = res;
    },
    submited(res) {
      if (res) {
        this.getList(1);
        this.getTopData();
      }
    },
    onClickBuy() {
      this.buyAssetsDlg = true;
    },
    showTip() {
      this.showQuotation = true;
    },
    //提交表单按钮
    submit(row) {
      //修改碳信用状态
      this.form.assetType = "0140000001";
      const data = {
        id: this.form.id,
        transactionStatus: "0160000001",
      };
      if (this.form.tradeQuantity > this.sellCarbonTotal) {
        this.$message.warning("出售数量不能大于可用量");
        return;
      }
      if (this.form.tradeQuantity) {
        credit.addcarbonAssetMarket(this.form).then(
          (res) => {
            credit.changeCredit(data).then(
              (res) => {
                this.$message.success("操作成功");
                this.showTip();
                this.dialogFormVisible = false;
                this.getList(1);
              },
              (err) => {
                this.$message.error(err.msg);
              }
            );
          },
          (err) => {}
        );
      } else {
        this.$message.warning("必填项不能为空");
      }
    },
    toQuotation() {
      this.$router.push("/trade/quotation");
    },
    //碳信用点击事件
    handle(row, column) {
      if (column.label != "操作") {
        this.toDetail(row.id);
      }
    },
    toDetail(id) {
      this.$router.push({
        path: "/assets/credit/assetDetail",
        query: { id: id },
      });
    },
    pickExchangeMethod(method) {
      switch (method) {
        case "竞价交易":
          return "0190000001";
        case "定价交易":
          return "0190000002";
        case "协议转让":
          return "0190000003";
      }
    },
    //通过项目搜索
    searchByProject() {
      const data = {
        projectName: this.searchProjectKeyword,
        certificationCriteria: this.seletedStandard[0], //当前选择的标准
        transactionStatus: this.seletedStatus[0], //当前选择的状态
        issuingDateStart: this.selectDate,
        assetsStatus: this.seletedAssetsStatus[0],
        projectScopeCode: this.seletedIndustry[0],
        issuingDateEnd: this.selectEndDate,
        assetsStatus: this.seletedAssetsStatus[0],
      };
      credit.loadCarbonCreditPageList(data).then(
        (res) => {
          this.list = res.data.records;
          this.list.map((v) => {
            v.industryCodeName = v.industryCodeName ? v.industryCodeName : "--";
            let time = v.issuingDate.split(" ");
            v.issuingDate = time[0];
            for (var i in v) {
              v[i] = v[i] ? v[i] : "--";
            }
          });
        },
        (err) => {}
      );
    },
    //通过方法学搜素
    searchByMethod() {
      const queryData = {
        methodName: this.searchMethodKeyword,
        certificationCriteria: this.seletedStandard[0], //当前选择的标准
        transactionStatus: this.seletedStatus[0], //当前选择的状态
        issuingDateStart: this.selectDate,
        assetsStatus: this.seletedAssetsStatus[0],
        projectScopeCode: this.seletedIndustry[0],
        issuingDateEnd: this.selectEndDate,
        assetsStatus: this.seletedAssetsStatus[0],
      };
      credit.loadCarbonCreditPageList(queryData).then(
        (res) => {
          this.list = res.data.records;
          this.list.map((v) => {
            v.industryCodeName = v.industryCodeName ? v.industryCodeName : "--";
            let time = v.issuingDate.split(" ");
            v.issuingDate = time[0];
            for (var i in v) {
              v[i] = v[i] ? v[i] : "--";
            }
          });
        },
        (err) => {}
      );
    },
    onEdit() {},
    onClickPublish() {},
    onClickDelete() {},
    onClickOffline() {},
    handleSizeChange(val) {
      this.pageSize = val;
      this.getList(1);
    },
    handleCurrentChange(val) {
      this.current = val;
      this.getList(val);
    },
    // 监听页面宽度变化，刷新表格
    handleResize() {
      if (this.infoList) this.$refs.visitChart.handleResize();
    },
    //场内交易
    insideTransaction() {
      this.$router.push("/trade/account/exchange");
    },
    update() {
      const data = {
        certificationCriteria: this.seletedStandard[0], //当前选择的标准
        transactionStatus: this.seletedStatus[0], //当前选择的状态
        issuingDateStart: this.selectDate,
        projectName: this.searchProjectKeyword,
        methodName: this.searchMethodKeyword,
        assetsStatus: this.seletedAssetsStatus[0],
        projectScopeCode: this.seletedIndustry[0],
        issuingDateEnd: this.selectEndDate,
        assetsStatus: this.seletedAssetsStatus[0],
      };
      loadCarbonCreditPageList(data)
        .then((res) => {
          this.list = res.data.records;
          this.total = Number(res.data.total);
          this.current = Number(res.data.current);
          this.pageCount = Math.ceil(parseInt(res.data.total) / this.pageSize);
          this.list.map((v) => {
            v.industryCodeName = v.industryCodeName ? v.industryCodeName : "--";
            if (v.issuingDate) {
              let time = v.issuingDate.split(" ");
              v.issuingDate = time[0];
            }
            for (var i in v) {
              v[i] = v[i] ? v[i] : "--";
            }
          });
        })
        .catch((errror) => {});
    },
    getList(page) {
      const data = {
        asc: true,
        current: page,
        size: this.pageSize,
        // "status": 0,
        // "type": 0
      };
      loadCarbonCreditPageList(data)
        .then((res) => {
          if (res.data.records) {
            this.list = res.data.records;
          }
          this.total = Number(res.data.total);
          // console.log(res.data.total);
          this.current = Number(res.data.current);
          this.pageCount = Math.ceil(parseInt(this.total) / this.pageSize);
          this.list.map((v) => {
            //遍历表格数据
            // v.industryCodeName = v.industryCodeName ? v.industryCodeName : "--";
            if (v.issuingDate) {
              let time = v.issuingDate.split(" ");
              v.issuingDate = time[0];
            }
            // v.projectName = v.projectName?v.projectName:"--"
            // v.certificationCriteriaName = v.certificationCriteriaName?v.certificationCriteriaName:"--"
            // v.transactionStatusName = v.transactionStatusName?v.transactionStatusName:"--"
            for (var i in v) {
              v[i] = v[i] ? v[i] : "--";
              if (v[i] == " ") {
                v[i] = "--";
              }
            }
          });
        })
        .catch((errror) => {});
    },
    OutsizeTransaction(row) {
      debugger;
      this.dialogFormVisible = true;
      this.title = "场外上架：" + row.projectName;
      this.form.id = row.id;
      this.form.projectId = row.carbonProjectId;
      this.form.assetType = row.transactionStatus;
      this.form.institutionName = row.projectName;
      this.form.projectType = row.fieldChildCode;
      this.sellCarbonTotal = row.availableAmount;
    },
    // checkbox start
    signCheckChange() {
      let allCheckedFlag = true;
      let allReset = true;
      this.articals.forEach((item) => {
        if (item.checked == true) {
          allReset = false;
        } else {
          allCheckedFlag = false;
        }
      });
      if (allCheckedFlag || allReset) {
        this.indeterminateFlag = false;
        if (allCheckedFlag) {
          this.allchecked = true;
        } else {
          this.allchecked = false;
        }
      } else {
        this.indeterminateFlag = true;
      }
      this.reRender = !this.reRender;
    },

    //render-header方法
    renderCheckHeader(h, { column, $index }) {
      return h("span", {}, [
        h("el-checkbox", {
          props: {
            checked: this.allchecked,
            indeterminate: this.indeterminateFlag, //表头复选框状态
          },
          on: {
            change: this.updateAllSelected, // 选中事件
          },
        }),
      ]);
    },
    // 格式化验证标准字典
    formatCertification(data) {
      data.map((v) => {
        let CertificationItem = {
          label: "",
        };
        if (v.name == "全部") {
          CertificationItem.label = v.name;
        } else {
          CertificationItem.value = v.value;
          CertificationItem.label = v.name;
        }
        this.optionsStandard.push(CertificationItem);
      });
    },
    // 格式化行业类型字典
    formatIndustory(data) {
      data.map((v) => {
        let CertificationItem = {
          label: "",
        };
        if (v.name == "全部") {
          CertificationItem.label = v.name;
        } else {
          CertificationItem.value = v.value;
          CertificationItem.label = v.name;
        }
        this.optionsIndustory.push(CertificationItem);
      });
    },
    // 格式化状态类型字典
    formatStatus(data) {
      data.map((v) => {
        let CertificationItem = {
          label: "",
        };
        if (v.name == "全部") {
          CertificationItem.label = v.name;
        } else {
          CertificationItem.value = v.value;
          CertificationItem.label = v.name;
        }
        this.optionsOnlines.push(CertificationItem);
      });
    },
    // 格式化状态类型字典
    formatAssetStatus(data) {
      data.map((v) => {
        let CertificationItem = {
          label: "",
        };
        if (v.name == "全部") {
          CertificationItem.label = v.name;
        } else {
          CertificationItem.value = v.value;
          CertificationItem.label = v.name;
        }
        this.optionsAssetStatus.push(CertificationItem);
      });
    },
    //导出功能
    onClickExport() {
      this.$message.success("功能即将推出，敬请期待！");
    },
    //拿到资产数据
    getTopData() {
      if (this.flag == 1) {
        return;
      }
      this.flag == 1;
      credit.getCreditData().then((res) => {
        if (res.data) {
          this.topData = res.data;
        }
        this.flag = 0;
      });
    },
  },
  created() {
    // this.handleChangeVisitType();
  },

  mounted() {
    this.getList(1);
    this.getTopData();
    this.exchangeList = getExchangeDict(this.$store);
    this.tradeMethods = getDiliveryMethodeDict(this.$store);
    let Certification = getCertificationCriteriaDict(this.$store);
    this.formatCertification(Certification);
    this.formatIndustory(getProjectAreaDict(this.$store));
    this.formatStatus(getAssetTradeStatusDict(this.$store));
    this.formatAssetStatus(getAssetStatusDict(this.$store));
  },
};
</script>
<style lang="scss" scoped>
.root {
  background: #f2f5f7;
}

.divBox {
  margin: 20px;
  background: #ffffff;
  box-shadow: 0px 2px 8px 0px #eaf0f3;
  border-radius: 8px;
}

.container {
  margin: 10px 0px 20px 0px;
  display: flex;
  flex-direction: row;
}

.content-container {
  display: flex;
  flex-direction: column;
  width: 100%;
}

>>> .el-cascader .el-input .el-input__inner,
>>> .el-cascader .el-input.is-focus .el-input__inner {
  border-color: transparent;
}

::v-deep.el-date-picker.has-sidebar.has-time {
  background: #0a5857d6;
  color: #fff;
  border: 1px solid #22f4d6;
}

::v-deep.el-date-picker__header-label {
  color: #ffffff;
}

.acea-row {
  ::v-deep.el-avatar--small {
    width: 22px;
    height: 22px;
    line-height: 22px;
  }
}

.checkTime {
  ::v-deep.el-radio__input {
    display: none;
  }
}

.ivu-pl-8 {
  margin-left: 8px;
  font-size: 14px;
}

.dashboard-console-visit {
  ::v-deep.el-card__header {
    padding: 14px 20px !important;
  }

  ul {
    li {
      list-style-type: none;
      margin-top: 12px;
    }
  }
}

.ivu-mb {
  margin-bottom: 10px;
}

.newsImg {
  width: 30px;
  height: 30px;
  border-radius: 4px;
}

.myassets-div {
  width: 184px;
  display: flex;
  flex-direction: row;
}

.icon {
  width: 24px;
  height: 24px;
}

.text-left {
  margin: auto;
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  cursor: default;
  font-weight: 500;
  color: #24a776;
}

.myassets-container {
  display: flex;
  flex-direction: row;
  margin-top: 15px;
  margin-bottom: 20px;
  padding-left: 10px;
  padding-right: 10px;
  height: 54px;
  background: #e3f2ec;
  border-radius: 6px;
  // opacity: 0.1;
}

.assets-hint {
  margin-top: auto;
  margin-bottom: auto;
  font-size: 16px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #424c5c;
  line-height: 16px;
}

.assets-text {
  margin-top: auto;
  margin-bottom: auto;
  margin-left: 6px;
  font-size: 17px;
  font-family: Barlow-Medium, Barlow;
  font-weight: 500;
  color: #24a776;
  line-height: 17px;
}

.assets-line {
  margin: auto;
  margin-left: 10px;
  margin-right: 10px;
  width: 1px;
  height: 18px;
  border: 1px solid rgba(38, 181, 129, 0.5);
}

.center-vertical {
  margin-top: auto;
  margin-bottom: auto;
}

.require {
  color: red;
  font-size: 16px;
  position: relative;
  right: 20px;
}
</style>
