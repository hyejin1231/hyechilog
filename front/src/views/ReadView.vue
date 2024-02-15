<script setup lang="ts">
import {defineProps, onMounted, ref} from 'vue'
import axios from "axios";
import {useRouter} from "vue-router";

const props = defineProps({
  blogId : {
    type : [Number, String],
    require : true
  }
})

const blog = ref({
  id : 0,
  title : "",
  content : ""
});

onMounted(() => {
  axios.get(`/my-backend/api/v1/blogs/${props.blogId}`).then((response) => {
     blog.value = response.data.data;
  })
})

const router = useRouter();

const moveToEdit = () => {
  router.push({name: 'edit', params: {blogId : props.blogId}})
}

</script>

<template>
  <el-row>
    <el-col>
      <h2 class="title">
        {{blog.title}}
      </h2>

      <div class="sub d-flex">
        <div class="category">
          개발
        </div>
        <div class="regDate">
          2024-02-15 23:59:59
        </div>
      </div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="content">
        {{blog.content}}
      </div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit()"> 수정하기 </el-button>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.title {
  color: #383838;
  font-size: 1.6rem;
  font-weight: 600;
  margin: 0;
}

.sub {
  margin-top : 10px;
  font-size: 0.78rem;

  .regDate {
    margin-left: 10px;
    color: #6b6b6b;
  }
}

.content {
  margin-top: 12px;
  font-size: 0.95rem;
  color: #616161;
  white-space: break-spaces;
  line-height: 1.5;
}


</style>