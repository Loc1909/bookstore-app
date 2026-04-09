import UserTable from "../components/ui/UserTable";


export default function UserTablePage() {
    return (
        <>
            <main>
                <div class="p-4 mx-auto max-w-(--breakpoint-2xl) md:p-6">
                    <div x-data="{ pageName: `Basic Tables`}">
                        <include src="./partials/breadcrumb.html" />
                    </div>
                    <div class="space-y-5 sm:space-y-6">
                        <div class="rounded-2xl border border-gray-200 bg-white dark:border-gray-20 dark:bg-white/3" >
                            <div class="px-5 py-4 sm:px-6 sm:py-5">
                                <h3 class="text-base font-medium text-gray-800 dark:text-gray-500" >
                                    User Table 
                                </h3>
                            </div>
                            <UserTable />
                        </div>
                    </div>
                </div>
            </main>
        </>
    );
}