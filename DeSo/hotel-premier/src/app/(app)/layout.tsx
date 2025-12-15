import Navbar from "@/components/navbar";
import Sidebar from "@/components/sidebar";
import Footer from "@/components/footer";

export default function AppLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="h-screen flex overflow-hidden bg-[#f5f7fa] dark:bg-gray-950 text-[#333] dark:text-white">
      <Sidebar />
      <div className="flex-1 flex flex-col h-full relative">
        <Navbar />
        <main className="flex-1 overflow-y-auto p-4 bg-[#f5f7fa] dark:bg-gray-950">
          <div className="max-w-7xl mx-auto w-full">{children}</div>
          <div className="mt-10">
            <Footer />
          </div>
        </main>
      </div>
    </div>
  );
}
