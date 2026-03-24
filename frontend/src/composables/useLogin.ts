import { ref } from 'vue';
import authService from '@/services/auth.service';
import { toast } from 'vue3-toastify';
import {useRouter} from 'vue-router';



export function useLogin() {
    const username = ref('');
const password = ref('');
const loading = ref(false);
const router = useRouter();
    
const handleLogin = async () => {
    if(!username.value || !password.value){
        toast.error('Please enter both username and password');
        return;
    }
    loading.value = true;
    try {
        await authService.login(username.value, password.value);
        toast.success('Login successful!');
        setTimeout(() => {
        router.push('/home'); 
      }, 1000);
    }
    catch (error) {
        toast.error('Login failed. Please check your credentials and try again.');
    }
    finally {
        loading.value = false;
    }
};

return {
    username,
    password,
    loading,
    handleLogin,
};
}
