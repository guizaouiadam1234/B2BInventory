import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/components/Login.vue';
import Home from '@/components/Home.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path : '/', component : Login},
    {path : '/home', component : Home},
  ],
})
router.beforeEach((to, from, next) => {
  const publicPages = ['/']; // Liste des pages accessibles sans login
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = localStorage.getItem('user_token');

  // Si la page demande une auth et que l'utilisateur n'est pas connecté
  if (authRequired && !loggedIn) {
    return next('/'); // On le renvoie au Login
  }

  next();
});

export default router;
