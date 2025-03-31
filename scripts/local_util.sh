# Function to echo a cool header
echo_header() {
  local header_text=$1
  local border="*****************************************************************"
  local purple='\033[0;36m'
    local no_color='\033[0m'

  echo -e "${purple}${border}${no_color}"
  echo -e "${purple}$header_text${no_color}"
  echo ""
}

echo_footer() {
  local footer_text=$1
  local purple='\033[0;36m'
    local no_color='\033[0m'

  echo -e "${purple}$footer_text${no_color}"
  echo ""
}

echo_app_title() {
  local app_title="auth service"
  local purple='\033[0;36m'
  local no_color='\033[0m'

  echo -e "${purple}    ___         __  __       _____                 _         ${no_color}"
  echo -e "${purple}   /   | __  __/ /_/ /_     / ___/___  ______   __(_)_______ ${no_color}"
  echo -e "${purple}  / /| |/ / / / __/ __ \    \__ \/ _ \/ ___/ | / / / ___/ _ \ ${no_color}"
  echo -e "${purple} / ___ / /_/ / /_/ / / /   ___/ /  __/ /   | |/ / / /__/  __/${no_color}"
  echo -e "${purple}/_/  |_\__,_/\__/_/ /_/   /____/\___/_/    |___/_/\___/\___/ ${no_color}"

  echo ""
}