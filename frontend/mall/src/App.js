import { RouterProvider } from 'react-router-dom';
import root from "./router/root";
import './App.css';
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
// @tanstack/react-query의 핵심 기능: 서버에서 받아온 데이터를 캐시하고, 재사용하고, 상태 관리
const queryClient = new QueryClient();
function App() {
  return (
      <QueryClientProvider client={queryClient}> {/* React Query 상태 관리(Caching, Fetch, Mutation 등) 사용 가능하게 함  */}
    <RouterProvider router={root}></RouterProvider>
     <ReactQueryDevtools initialIsOpen={true} /> {/*React Query의 개발자용 디버깅 도구 */} 
    </QueryClientProvider>
  );
}

export default App;
