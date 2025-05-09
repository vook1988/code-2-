<template>
  <div class="upload-container">
    <el-button type="primary" @click="openUploadDialog">上传图像</el-button>
    <!-- 图像列表（一行四张） -->
    <div class="image-list">
      <div v-for="image in imageList" :key="image.imageId" class="image-item" @click="goToAnnotate(image.imageId)">
        <img :src="getImageUrl(image.storagePath)" alt="上传的图像">
        <div class="status">
          {{ image.annotated ? '已标注' : '未标注' }}
        </div>
      </div>
    </div>
    <!-- 上传对话框 -->
    <el-dialog v-model="dialogVisible" title="选择上传图像">

      <el-upload ref="uploadRef" :action="uploadUrl" :on-change="handleImageChange" :show-file-list="false"
        :before-upload="beforeUpload" :auto-upload="false" :on-success="handleUploadSuccess" :data="{ userId: userId }"
        accept="image/*">
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传至服务器</el-button>

      </el-upload>


      <!-- 新增预览区域 -->
      <div v-if="previewUrl" class="preview" style="margin-top: 15px;">
        <img :src="previewUrl" alt="上传预览" style="max-width: 300px; border: 1px solid #eee;">
        <div style="font-size: 12px; margin-top: 5px;">上传前预览（仅本地临时显示）</div>
        <el-button size="small" type="default" @click="cancelUpload">取消</el-button>
      </div>
      <div>

      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const imageList = ref([]);
const dialogVisible = ref(false);
const uploadRef = ref(null);
const uploadUrl = ref('http://localhost:8080/image/upload');
const previewUrl = ref('');
const userId = ref(localStorage.getItem('userId') || 0);

// 生成图片 URL
const getImageUrl = (storagePath) => {
  if (!storagePath) {
    console.error('Invalid storagePath:', storagePath);
    return '';
  }
  return `http://localhost:8080/${storagePath}`;
};

// 初始化加载图像列表
onMounted(async () => {
  await fetchImages();
});

// 获取图像列表
// 获取图像列表时使用后端接口
const fetchImages = async () => {
  try {
    const response = await axios.get('http://localhost:8080/image/list');
    imageList.value = response.data.data;
  } catch (error) {
    ElMessage.error('获取图片失败');
  }
};

const handleImageChange = (file) => {
  // 读取文件生成DataURL用于预览
  const reader = new FileReader();
  reader.onload = (e) => {
    previewUrl.value = e.target.result; // 赋值给预览URL
  };
  reader.readAsDataURL(file.raw); // 读取选中的文件
};

// 上传成功后刷新列表
const handleUploadSuccess = (response) => {
  if (response.code === 200) {
    ElMessage.success('上传成功');
    fetchImages(); // 刷新图像列表
    cancelUpload(); // 清空上传状态
  }
};

// 跳转标注页面
const goToAnnotate = (imageId) => {
  router.push({
    path: '/annotate',
    query: { imageId: imageId }
  });
};

const openUploadDialog = () => dialogVisible.value = true;

const beforeUpload = (file) => {
  const isJPG = file.type === 'image/jpeg';
  const isGIF = file.type === 'image/gif';
  const isPNG = file.type === 'image/png';
  const isBMP = file.type === 'image/bmp';
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isJPG && !isGIF && !isPNG && !isBMP) {
    ElMessage.error('上传图片必须是JPG/GIF/PNG/BMP 格式!');
    return false;
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!');
    return false;
  }
  return true;
};
const cancelUpload = () => {
  uploadRef.value.clearFiles(); // 清空已选文件
  previewUrl.value = ''; // 清空预览
  dialogVisible.value = false; // 关闭对话框
};
const submitUpload = () => {
  uploadRef.value.submit(); // 手动触发上传
};
</script>

<style scoped>
.image-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-top: 20px;
}

.image-item {
  position: relative;
  cursor: pointer;
}

.image-item img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}
</style>