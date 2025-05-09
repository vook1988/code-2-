<template>
    <div class="mod-robot">
      <el-form :inline="true" :model="searchData" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="searchData.robotCode" placeholder="机器人编码" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="searchData.name" placeholder="机器人名称" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="searchData.status" placeholder="状态" clearable>
            <el-option label="全部" value=""></el-option>
            <el-option label="活跃" value="active"></el-option>
            <el-option label="维护中" value="maintenance"></el-option>
            <el-option label="停用" value="inactive"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()" type="primary">搜索</el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="addOrUpdateHandle()" type="primary" >新增</el-button>
        </el-form-item>
      </el-form>
  
      <el-table
        :data="dataList"
        border
        v-loading="dataListLoading"
        style="width: 100%;">
        <el-table-column
          prop="robotCode"
          header-align="center"
          align="center"
          label="机器人编码">
        </el-table-column>
        <el-table-column
          prop="name"
          header-align="center"
          align="center"
          label="名称">
        </el-table-column>
        <el-table-column
          prop="model"
          header-align="center"
          align="center"
          label="型号">
        </el-table-column>
        <el-table-column
          prop="manufacturer"
          header-align="center"
          align="center"
          label="制造商">
        </el-table-column>
        <el-table-column
          prop="status"
          header-align="center"
          align="center"
          label="状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'active'" type="success">活跃</el-tag>
            <el-tag v-if="scope.row.status === 'maintenance'" type="warning">维护中</el-tag>
            <el-tag v-if="scope.row.status === 'inactive'" type="danger">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="currentLocation"
          header-align="center"
          align="center"
          label="当前位置">
        </el-table-column>
        <el-table-column
          prop="batteryLevel"
          header-align="center"
          align="center"
          label="电量">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.batteryLevel" :color="getBatteryColor(scope.row.batteryLevel)"></el-progress>
          </template>
        </el-table-column>
        <el-table-column
          prop="lastMaintenance"
          header-align="center"
          align="center"
          label="上次维护时间">
          <template slot-scope="scope">
            {{scope.row.lastMaintenance }}
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          header-align="center"
          align="center"
          width="200"
          label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="showDetail(scope.row.robotId)">查看</el-button>
            <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.robotId)">修改</el-button>
            <el-button type="text" size="small" @click="deleteHandle(scope.row.robotId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-pagination
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
        :current-page="pageIndex"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="totalPage"
        layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>
  
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
      
      <!-- 详情弹窗 -->
      <el-dialog title="机器人详情" :visible.sync="detailVisible" width="70%">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="机器人编码">{{detailData.robotCode}}</el-descriptions-item>
          <el-descriptions-item label="名称">{{detailData.name}}</el-descriptions-item>
          <el-descriptions-item label="型号">{{detailData.model}}</el-descriptions-item>
          <el-descriptions-item label="制造商">{{detailData.manufacturer}}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag v-if="detailData.status === 'active'" type="success">活跃</el-tag>
            <el-tag v-if="detailData.status === 'maintenance'" type="warning">维护中</el-tag>
            <el-tag v-if="detailData.status === 'inactive'" type="danger">停用</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="当前位置">{{detailData.currentLocation}}</el-descriptions-item>
          <el-descriptions-item label="电量">
            <el-progress :percentage="detailData.batteryLevel" :color="getBatteryColor(detailData.batteryLevel)"></el-progress>
          </el-descriptions-item>
          <el-descriptions-item label="采购日期">{{detailData.purchaseDate }}</el-descriptions-item>
          <el-descriptions-item label="上次维护">{{detailData.lastMaintenance }}</el-descriptions-item>
          <el-descriptions-item label="下次维护">{{detailData.nextMaintenance }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{detailData.createdAt }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{detailData.updatedAt }}</el-descriptions-item>
        </el-descriptions>
        <span slot="footer" class="dialog-footer">
          <el-button @click="detailVisible = false">关闭</el-button>
        </span>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import AddOrUpdate from './robot-add-or-update'
//   import { formatDate } from '@/utils/format'
  
  export default {
    data () {
      return {
        dataList: [],
        detailData: {},
        searchData: {
          robotCode: '',
          name: '',
          status: '',
          typeId: null
        },
        dataListLoading: false,
        addOrUpdateVisible: false,
        detailVisible: false,
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0
      }
    },
    components: {
      AddOrUpdate
    },
    filters: {
      formatDate (time) {
        if (!time) return ''
        return formatDate(new Date(time), 'yyyy-MM-dd hh:mm')
      }
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/robot/list'),
          method: 'get',
          params: this.$http.adornParams({
            'pageNum': this.pageIndex,
            'pageSize': this.pageSize,
            'robotCode': this.searchData.robotCode,
            'name': this.searchData.name,
            'status': this.searchData.status,
            'typeId': this.searchData.typeId
          })
        }).then(({data}) => {
          if (data && data.success) {
            this.dataList = data.data
            this.totalPage = data.total
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      // 显示详情
      showDetail (id) {
        this.$http({
          url: this.$http.adornUrl(`/robot/detail`),
          method: 'get',
          params: this.$http.adornParams({
            'id': id
          })
        }).then(({data}) => {
          if (data && data.success) {
            this.detailData = data.data
            this.detailVisible = true
          } else {
            this.$message.error(data.msg || '获取详情失败')
          }
        })
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        this.$confirm(`确定对[id=${id}]进行[删除]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/robot/delete'),
            method: 'get',
            params: this.$http.adornParams({
              'id': id
            })
          }).then(({data}) => {
            if (data && data.success) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }).catch(() => {})
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 获取电池颜色
      getBatteryColor (percentage) {
        if (percentage > 70) {
          return '#67C23A'
        } else if (percentage > 30) {
          return '#E6A23C'
        } else {
          return '#F56C6C'
        }
      }
    }
  }
  </script>
  
  <style scoped>
  .mod-robot {
    padding: 20px;
  }
  .el-progress {
    min-width: 80px;
  }
  </style>