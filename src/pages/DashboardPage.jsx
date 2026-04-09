import Chart01 from "../components/ui/Chart01";
import Chart02 from "../components/ui/Chart02";
import Chart03 from "../components/ui/Chart03";
import Map1 from "../components/ui/Map1";
import MetricGroup from "../components/ui/MetricGroup";
import Table1 from "../components/ui/table1";

export default function DashboardPage() {
    return (
        <>
            <main>
                <div className="p-4 mx-auto max-w-(--breakpoint-2xl) md:p-6">
                    <div className="grid grid-cols-12 gap-4 md:gap-6">
                        <div className="col-span-12 space-y-6 xl:col-span-7">
                            <MetricGroup />
                            <Chart01 />
                        </div>
                        <div className="col-span-12 xl:col-span-5">
                            <Chart02 />
                        </div>
                        <div className="col-span-12">
                            <Chart03 />
                        </div>
                        <div className="col-span-12 xl:col-span-5">
                            <Map1 />
                        </div>
                        <div className="col-span-12 xl:col-span-7">
                            <Table1 />
                        </div>
                    </div>
                </div>
            </main>

        </>
    );
}