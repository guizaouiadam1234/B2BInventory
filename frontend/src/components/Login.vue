<script setup lang="ts">
import { useLogin } from '../composables/useLogin';

const { username, password, loading, handleLogin } = useLogin();
</script>

<template>
  <div class="min-h-screen bg-slate-950 flex items-center justify-center p-4">
    <div class="w-full max-w-md bg-slate-900 rounded-2xl shadow-2xl border border-slate-800 p-10">
      
      <h2 class="text-3xl font-extrabold text-slate-50 text-center mb-8">B2B Inventory</h2>
      
      <form @submit.prevent="handleLogin" class="space-y-5">
        <input 
          v-model="username" 
          type="text" 
          placeholder="Username" 
          class="w-full bg-slate-800 border border-slate-700 text-slate-200 px-4 py-3 rounded-xl outline-none focus:ring-2 focus:ring-indigo-500" 
        />
        
        <input 
          v-model="password" 
          type="password" 
          placeholder="Password" 
          class="w-full bg-slate-800 border border-slate-700 text-slate-200 px-4 py-3 rounded-xl outline-none focus:ring-2 focus:ring-indigo-500" 
        />

        <button 
          type="submit"
          :disabled="loading"
          class="w-full bg-indigo-600 hover:bg-indigo-500 text-white font-bold py-3 rounded-xl transition-all disabled:opacity-50"
        >
          {{ loading ? 'Loading...' : 'Login' }}
        </button>
      </form>
      
    </div>
  </div>
</template>