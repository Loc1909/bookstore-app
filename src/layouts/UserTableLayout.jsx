import { Outlet } from "react-router-dom";
import Sidebar from "../components/layout/Sidebar";

export default function UserTableLayout() {
    return (
        <>
            <div className="flex h-screen overflow-hidden">
                <aside className="relative z-9999 shrink-0">
                    <Sidebar />
                </aside>
                <main className="relative flex flex-1 flex-col overflow-y-auto overflow-x-hidden">
                    <Outlet />
                </main>
            </div>
        </>
    );
}
