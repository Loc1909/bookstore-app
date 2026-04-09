import { Outlet, useLocation } from "react-router-dom";
import Sidebar from "../components/layout/Sidebar";

export default function DashboardLayout() {
    const pathname = useLocation();
    return (
        <>
            <div className="flex h-screen overflow-hidden">
                {/* Sidebar, Header, main(Outlet), ..., ButtonGoToTop,... */}
                <aside className="relative z-9999 shrink-0">
                    <Sidebar />
                </aside>
                <main className="relative flex flex-1 flex-col overflow-y-auto overflow-x-hidden">
                    <div className="mx-auto max-w-screen-2xl p-4 md:p-6 2xl:p-10">
                        <Outlet />
                    </div>
                </main>
            </div>
        </>
    );
}