<template>
    <el-select
        :modelValue="currentStatusName"
        @update:modelValue="updateStatus"
        style="width: 120px"
    >
        <el-option
            v-for="status in statusArr"
            :key="status.value"
            :label="status.key"
            :value="status.value"
        >
        </el-option>
    </el-select>
</template>

<script>
import inboundStatus from '../router/inboundStatus';

export default {
    props:['modelValue','statusArr'],
    emits:['update:modelValue'],
    data(){
        return {
            inboundStatus,
            currentStatus: 0,
        }
    },
    computed: {
        currentStatusName: function() {
            return this.statusArr[this.currentStatus].key;
        }
    },
    methods: {
        updateStatus: function(val) {
            this.$emit('update:modelValue',val)
            this.currentStatus = val
        }
    }
}
</script>

<style>

</style>