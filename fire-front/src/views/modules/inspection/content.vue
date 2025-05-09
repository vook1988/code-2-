<template>
    <div class="app-container">
      <div class="search-container">
        <el-form inline>
          <el-form-item label="名称">
            <el-input />
          </el-form-item>
          <el-button type="primary">搜索</el-button>
          <el-button type="primary" @click="operate('add')">新增</el-button>
        </el-form>
      </div>
      <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row>
        <el-table-column align="center" label="ID" width="95">
          <template slot-scope="scope">
            {{ scope.$index }}
          </template>
        </el-table-column>
        <el-table-column label="名称">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column label="类目">
          <template slot-scope="scope">
            {{ scope.row.phone }}
          </template>
        </el-table-column>
        <el-table-column label="线路">
          <template slot-scope="scope">
            {{ scope.row.place }}
          </template>
        </el-table-column>
        <!-- <el-table-column class-name="status-col" label="是否在职" width="110" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status | statusFilter">{{ scope.row.status | statusFilterLabel }}</el-tag>
          </template>
        </el-table-column> -->
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <!-- <el-button type="primary" size="mini" :disabled="scope.row.status == 0">发布</el-button> -->
            <!-- <el-button type="warning" size="mini" :disabled="scope.row.status == 1">停用</el-button> -->
            <el-button type="success" size="mini" @click="operate('edit', scope.row)">修改</el-button>
            <el-button type="danger" size="mini">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog :visible.sync="dialogVisible" :title="title" width="500px">
        <el-form ref="form" :model="form" label-width="120px">
          <el-form-item label="名称">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="类目">
            <el-select v-model="form.person" placeholder="请选择部门">
              <el-option v-for="(item, index) in typeList" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="线路">
            <el-select v-model="form.num" placeholder="请选择职位">
              <el-option v-for="(item, index) in numList" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="是否在职">
            <el-switch v-model="form.delivery" />
          </el-form-item> -->
  
          <!-- <el-form-item label="Resources">
            <el-radio-group v-model="form.resource">
              <el-radio label="Sponsor" />
              <el-radio label="Venue" />
            </el-radio-group>
          </el-form-item>
          <el-form-item label="Activity form">
            <el-input v-model="form.desc" type="textarea" />
          </el-form-item> -->
          <el-form-item>
            <el-button type="primary" @click="onSubmit">提交</el-button>
            <el-button @click="onCancel">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </template>
  
  <script>
  //import { getList } from '@/api/table'
  
  export default {
    filters: {
      statusFilter(status) {
        const statusMap = {
          0: 'success',
          1: 'info'
        }
        return statusMap[status]
      },
      statusFilterLabel(status) {
        const statusMap = {
          0: '在职',
          1: '离职'
        }
        return statusMap[status]
      }
    },
    data() {
      return {
        list: null,
        listLoading: true,
        data: [{
          name: '食堂巡查',
          phone: '13788889999',
          dept: '财务',
          display_time: '2023-12-24 16:10:10',
          id: '450000199302104628',
          pageviews: 1165,
          status: 0,
          place: '经理'
        }, {
          name: '食堂巡查',
          phone: '13788889999',
          dept: '后勤',
          display_time: '2023-12-24 16:10:10',
          id: '450000199302104628',
          pageviews: 1165,
          status: 1,
          place: '员工'
        }, {
          name: '食堂巡查',
          phone: '13788889999',
          dept: '食堂',
          display_time: '2023-12-24 16:10:10',
          id: '450000199302104628',
          pageviews: 1165,
          status: 0,
          place: '员工'
        }],
        dialogVisible: false,
        title: '新增巡检内容',
        personList: [{ label: '张三', value: '0' },{ label: '张yi', value: '0' },{ label: '张四', value: '0' },{ label: '张5', value: '0' }],
        numList: [{ label: '食堂路线', value: '0' }, { label: '消防路线', value: '1' }, { label: '查房路线', value: '2' }],
        typeList: [{ label: '消防安全', value: '0' }, { label: '食堂安全', value: '1' }, { label: '护工查房', value: '2' }],
        form: {
        }
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      fetchData() {
        this.listLoading = true
        this.list = this.data
        this.listLoading = false
        // getList().then(response => {
        //   this.list = response.data.items
        //   this.listLoading = false
        // })
      },
      operate(type, row) {
        if (type === 'add') {
          this.dialogVisible = true
          this.title = '新增巡检内容'
        } else if (type === 'edit') {
          this.dialogVisible = true
          this.title = '修改巡检内容'
          this.form = JSON.parse(JSON.stringify(row))
        }
      },
      onSubmit() {
        this.$message('submit!')
      },
      onCancel() {
        this.dialogVisible = false
      }
    }
  }
  </script>
  <style scoped>
  .line {
    text-align: center;
  }
  </style>
  