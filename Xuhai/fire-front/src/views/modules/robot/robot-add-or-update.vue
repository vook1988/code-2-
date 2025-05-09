<template>
  <el-dialog :title="!dataForm.robotId ? '新增' : '修改'" :visible.sync="visible" width="60%" @close="closeDialog">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="机器人编码" prop="robotCode">
            <el-input v-model="dataForm.robotCode" placeholder="机器人编码"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="名称" prop="name">
            <el-input v-model="dataForm.name" placeholder="名称"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="类别" prop="model">
            <el-select v-model="dataForm.model" placeholder="请选择型号">
              <el-option label="机器狗" value="机器狗"></el-option>
              <el-option label="无人机" value="无人机"></el-option>
              <el-option label="巡检小车" value="巡检小车"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="制造商" prop="manufacturer">
            <el-input v-model="dataForm.manufacturer" placeholder="制造商"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="dataForm.status" placeholder="请选择状态">
              <el-option label="活跃" value="active"></el-option>
              <el-option label="维护中" value="maintenance"></el-option>
              <el-option label="停用" value="inactive"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="当前位置" prop="currentLocation">
            <el-input v-model="dataForm.currentLocation" placeholder="当前位置"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="电量" prop="batteryLevel">
            <el-input-number v-model="dataForm.batteryLevel" :min="0" :max="100" label="电量百分比"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="采购日期" prop="purchaseDate">
            <el-date-picker
              v-model="dataForm.purchaseDate"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="上次维护时间" prop="lastMaintenance">
            <el-date-picker
              v-model="dataForm.lastMaintenance"
              type="datetime"
              placeholder="选择日期时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="下次维护时间" prop="nextMaintenance">
            <el-date-picker
              v-model="dataForm.nextMaintenance"
              type="datetime"
              placeholder="选择日期时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="备注" prop="remark">
        <el-input type="textarea" v-model="dataForm.remark" placeholder="备注"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        robotId: null,
        robotCode: '',
        typeId: null,
        name: '',
        model: '',
        manufacturer: '',
        purchaseDate: '',
        status: 'active',
        currentLocation: '',
        batteryLevel: 100,
        lastMaintenance: '',
        nextMaintenance: '',
        remark: ''
      },
      dataRule: {
        robotCode: [
          { required: true, message: '机器人编码不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        model: [
          { required: true, message: '请选择型号', trigger: 'change' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.dataForm.robotId = id || null
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.robotId) {
          this.$http({
            url: this.$http.adornUrl(`/robot/detail`),
            method: 'get',
            params: this.$http.adornParams({
              'id': this.dataForm.robotId
            })
          }).then(({data}) => {
            if (data && data.success) {
              // 格式化日期数据
              const result = data.data
              if (result.purchaseDate) {
                result.purchaseDate = this.formatDate(result.purchaseDate, 'yyyy-MM-dd')
              }
              if (result.lastMaintenance) {
                result.lastMaintenance = this.formatDate(result.lastMaintenance, 'yyyy-MM-dd HH:mm:ss')
              }
              if (result.nextMaintenance) {
                result.nextMaintenance = this.formatDate(result.nextMaintenance, 'yyyy-MM-dd HH:mm:ss')
              }
              this.dataForm = result
            }
          })
        }
      })
    },
    
    // 日期格式化方法
    formatDate(date, format) {
      if (!date) return ''
      const d = new Date(date)
      const pad = num => (num < 10 ? '0' + num : num)
      
      return format
        .replace('yyyy', d.getFullYear())
        .replace('MM', pad(d.getMonth() + 1))
        .replace('dd', pad(d.getDate()))
        .replace('HH', pad(d.getHours()))
        .replace('mm', pad(d.getMinutes()))
        .replace('ss', pad(d.getSeconds()))
    },
    
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const request = this.dataForm.robotId 
            ? this.$http.adornUrl('/robot/update')
            : this.$http.adornUrl('/robot/add')
          
          const method = this.dataForm.robotId ? 'post' : 'post'
          
          // 准备提交数据
          const submitData = {
            ...this.dataForm
          }
          
          // 处理日期数据（如果需要转换为时间戳）
          // submitData.purchaseDate = new Date(submitData.purchaseDate).getTime()
          // submitData.lastMaintenance = new Date(submitData.lastMaintenance).getTime()
          // submitData.nextMaintenance = new Date(submitData.nextMaintenance).getTime()
          
          this.$http({
            url: request,
            method: method,
            data: this.$http.adornData(submitData)
          }).then(({data}) => {
            if (data && data.success) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    },
    
    closeDialog () {
      this.$refs['dataForm'].resetFields()
    }
  }
}
</script>