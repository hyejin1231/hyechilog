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
  <h2>
    {{blog.title}}
  </h2>

  <div>
    {{blog.content}}
  </div>

  <el-button type="warning" @click="moveToEdit()"> 수정하기 </el-button>
</template>